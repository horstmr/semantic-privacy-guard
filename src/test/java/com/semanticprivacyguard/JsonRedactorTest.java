package com.semanticprivacyguard;

import com.semanticprivacyguard.config.SPGConfig;
import com.semanticprivacyguard.structured.StructuredRedactionOutput;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for JSON redaction via {@link SemanticPrivacyGuard#redactJson}.
 *
 * <p>Requires {@code jackson-databind} on the test classpath (added as an
 * optional dependency in pom.xml, available during test compilation).</p>
 */
class JsonRedactorTest {

    private SemanticPrivacyGuard spg;

    @BeforeEach
    void setUp() {
        spg = SemanticPrivacyGuard.create();
    }

    // ── Basic redaction ───────────────────────────────────────────────────────

    @Test
    void email_inStringValue_redacted() throws IOException {
        StructuredRedactionOutput out = spg.redactJson(
            "{\"contact\": \"alice@acme.com\"}");

        assertFalse(out.getRedactedContent().contains("alice@acme.com"),
            "Email should be redacted");
        assertTrue(out.getRedactedContent().contains("[EMAIL_1]"),
            "Email token should be present");
        assertEquals(1, out.getMatchCount());
    }

    @Test
    void structurePreserved_afterRedaction() throws IOException {
        StructuredRedactionOutput out = spg.redactJson(
            "{\"name\": \"Alice Johnson\", \"age\": 34, \"active\": true}");

        String content = out.getRedactedContent();
        assertTrue(content.contains("\"age\":34"),
            "Numeric field should be preserved");
        assertTrue(content.contains("\"active\":true"),
            "Boolean field should be preserved");
    }

    @Test
    void noMatch_contentUnchangedAsJson() throws IOException {
        String input = "{\"status\": \"ok\", \"code\": 200}";
        StructuredRedactionOutput out = spg.redactJson(input);

        assertEquals(0, out.getMatchCount());
        assertFalse(out.hasPII());
        // Verify it round-trips as valid JSON (values unchanged, structure intact)
        assertFalse(out.getRedactedContent().contains("null"));
    }

    // ── Nested structure ──────────────────────────────────────────────────────

    @Test
    void nestedObject_allStringValuesScanned() throws IOException {
        String json = """
            {
              "user": {
                "name": "Bob Smith",
                "contact": {
                  "email": "bob.smith@corp.com",
                  "phone": "(555) 867-5309"
                }
              }
            }""";

        StructuredRedactionOutput out = spg.redactJson(json);

        String content = out.getRedactedContent();
        assertFalse(content.contains("bob.smith@corp.com"));
        assertFalse(content.contains("867-5309"));
        assertTrue(out.getMatchCount() >= 2,
            "Should detect at least email and phone");
    }

    @Test
    void arrayOfObjects_allRedacted() throws IOException {
        String json = """
            [
              {"email": "a@test.com"},
              {"email": "b@test.com"},
              {"email": "c@test.com"}
            ]""";

        StructuredRedactionOutput out = spg.redactJson(json);

        String content = out.getRedactedContent();
        assertFalse(content.contains("a@test.com"));
        assertFalse(content.contains("b@test.com"));
        assertFalse(content.contains("c@test.com"));
        assertEquals(3, out.getMatchCount());
    }

    // ── Document-scoped token counters ────────────────────────────────────────

    @Test
    void documentScopedCounters_emailsNumberedGlobally() throws IOException {
        String json = """
            {
              "from": "alice@acme.com",
              "to": "bob@corp.com"
            }""";

        StructuredRedactionOutput out = spg.redactJson(json);

        String content = out.getRedactedContent();
        // Both tokens must appear — numbering global not per-field
        assertTrue(content.contains("[EMAIL_1]"), "[EMAIL_1] expected");
        assertTrue(content.contains("[EMAIL_2]"), "[EMAIL_2] expected");
        assertEquals(2, out.getMatchCount());
    }

    // ── Reverse map ───────────────────────────────────────────────────────────

    @Test
    void reverseMap_populatedByDefault() throws IOException {
        StructuredRedactionOutput out = spg.redactJson(
            "{\"email\": \"alice@acme.com\"}");

        assertFalse(out.getReverseMap().isEmpty(),
            "Reverse map should be populated");
        assertTrue(out.getReverseMap().containsValue("alice@acme.com"),
            "Original email should be in reverse map");
    }

    @Test
    void reverseMap_suppressedWhenConfigured() throws IOException {
        SPGConfig config = SPGConfig.builder().buildReverseMap(false).build();
        SemanticPrivacyGuard spg2 = SemanticPrivacyGuard.create(config);

        StructuredRedactionOutput out = spg2.redactJson(
            "{\"email\": \"alice@acme.com\"}");

        assertTrue(out.getReverseMap().isEmpty(),
            "Reverse map should be empty when buildReverseMap=false");
    }

    // ── SSN in nested payload ─────────────────────────────────────────────────

    @Test
    void ssn_inNestedField_redacted() throws IOException {
        String json = """
            {
              "patient": {
                "ssn": "234-56-7890",
                "name": "Jane Doe"
              }
            }""";

        StructuredRedactionOutput out = spg.redactJson(json);

        assertFalse(out.getRedactedContent().contains("234-56-7890"));
        assertTrue(out.getMatchCount() >= 1);
    }

    // ── Edge cases ────────────────────────────────────────────────────────────

    @Test
    void nullInput_returnsEmpty() throws IOException {
        StructuredRedactionOutput out = spg.redactJson(null);
        assertEquals("", out.getRedactedContent());
        assertEquals(0, out.getMatchCount());
    }

    @Test
    void blankInput_returnsBlank() throws IOException {
        StructuredRedactionOutput out = spg.redactJson("   ");
        assertEquals("   ", out.getRedactedContent());
        assertEquals(0, out.getMatchCount());
    }

    @Test
    void invalidJson_throwsIOException() {
        assertThrows(IOException.class,
            () -> spg.redactJson("{not valid json"));
    }

    @Test
    void hasPII_falseWhenNoMatches() throws IOException {
        StructuredRedactionOutput out = spg.redactJson(
            "{\"status\": \"all clear\"}");
        assertFalse(out.hasPII());
    }

    @Test
    void hasPII_trueWhenMatchFound() throws IOException {
        StructuredRedactionOutput out = spg.redactJson(
            "{\"email\": \"user@example.com\"}");
        assertTrue(out.hasPII());
    }

    @Test
    void toString_containsMatchCount() throws IOException {
        StructuredRedactionOutput out = spg.redactJson(
            "{\"email\": \"user@example.com\"}");
        assertTrue(out.toString().contains("matches=1"));
    }
}

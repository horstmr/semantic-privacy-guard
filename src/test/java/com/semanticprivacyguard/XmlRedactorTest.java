package com.semanticprivacyguard;

import com.semanticprivacyguard.config.SPGConfig;
import com.semanticprivacyguard.structured.StructuredRedactionOutput;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for XML redaction via {@link SemanticPrivacyGuard#redactXml}.
 *
 * <p>No extra dependencies required — uses JDK {@code javax.xml}.</p>
 */
class XmlRedactorTest {

    private SemanticPrivacyGuard spg;

    @BeforeEach
    void setUp() {
        spg = SemanticPrivacyGuard.create();
    }

    // ── Basic redaction ───────────────────────────────────────────────────────

    @Test
    void email_inTextNode_redacted() throws IOException {
        StructuredRedactionOutput out = spg.redactXml(
            "<contact><email>alice@acme.com</email></contact>");

        assertFalse(out.getRedactedContent().contains("alice@acme.com"),
            "Email should be redacted");
        assertTrue(out.getRedactedContent().contains("[EMAIL_1]"),
            "Email token should be present");
        assertEquals(1, out.getMatchCount());
    }

    @Test
    void elementNamesPreserved_afterRedaction() throws IOException {
        StructuredRedactionOutput out = spg.redactXml(
            "<user><email>a@b.com</email><id>42</id></user>");

        String content = out.getRedactedContent();
        assertTrue(content.contains("<user>"), "Root element preserved");
        assertTrue(content.contains("<email>"), "Child element name preserved");
        assertTrue(content.contains("<id>42</id>"), "Non-PII content preserved");
    }

    @Test
    void noMatch_contentPassedThrough() throws IOException {
        String input = "<status><code>200</code><message>OK</message></status>";
        StructuredRedactionOutput out = spg.redactXml(input);

        assertEquals(0, out.getMatchCount());
        assertFalse(out.hasPII());
    }

    // ── Nested elements ───────────────────────────────────────────────────────

    @Test
    void nestedElements_allTextNodesScanned() throws IOException {
        String xml = """
            <patient>
              <name>Jane Doe</name>
              <ssn>234-56-7890</ssn>
              <contact>
                <email>jane.doe@hospital.org</email>
                <phone>(555) 123-4567</phone>
              </contact>
            </patient>""";

        StructuredRedactionOutput out = spg.redactXml(xml);

        String content = out.getRedactedContent();
        assertFalse(content.contains("234-56-7890"), "SSN should be redacted");
        assertFalse(content.contains("jane.doe@hospital.org"), "Email should be redacted");
        assertFalse(content.contains("123-4567"), "Phone should be redacted");
        assertTrue(out.getMatchCount() >= 3);
    }

    // ── Attribute values ──────────────────────────────────────────────────────

    @Test
    void email_inAttributeValue_redacted() throws IOException {
        StructuredRedactionOutput out = spg.redactXml(
            "<user email=\"bob@corp.com\" active=\"true\"/>");

        assertFalse(out.getRedactedContent().contains("bob@corp.com"),
            "Email in attribute should be redacted");
        assertTrue(out.getRedactedContent().contains("active=\"true\""),
            "Non-PII attribute preserved");
        assertEquals(1, out.getMatchCount());
    }

    // ── Document-scoped token counters ────────────────────────────────────────

    @Test
    void documentScopedCounters_emailsNumberedGlobally() throws IOException {
        String xml = """
            <contacts>
              <contact><email>alice@a.com</email></contact>
              <contact><email>bob@b.com</email></contact>
            </contacts>""";

        StructuredRedactionOutput out = spg.redactXml(xml);

        String content = out.getRedactedContent();
        assertTrue(content.contains("[EMAIL_1]"), "[EMAIL_1] expected");
        assertTrue(content.contains("[EMAIL_2]"), "[EMAIL_2] expected");
        assertEquals(2, out.getMatchCount());
    }

    // ── Reverse map ───────────────────────────────────────────────────────────

    @Test
    void reverseMap_populatedByDefault() throws IOException {
        StructuredRedactionOutput out = spg.redactXml(
            "<msg><from>alice@acme.com</from></msg>");

        assertFalse(out.getReverseMap().isEmpty());
        assertTrue(out.getReverseMap().containsValue("alice@acme.com"));
    }

    @Test
    void reverseMap_suppressedWhenConfigured() throws IOException {
        SPGConfig config = SPGConfig.builder().buildReverseMap(false).build();
        SemanticPrivacyGuard spg2 = SemanticPrivacyGuard.create(config);

        StructuredRedactionOutput out = spg2.redactXml(
            "<msg><from>alice@acme.com</from></msg>");

        assertTrue(out.getReverseMap().isEmpty());
    }

    // ── Edge cases ────────────────────────────────────────────────────────────

    @Test
    void nullInput_returnsEmpty() throws IOException {
        StructuredRedactionOutput out = spg.redactXml(null);
        assertEquals("", out.getRedactedContent());
        assertEquals(0, out.getMatchCount());
    }

    @Test
    void blankInput_returnsBlank() throws IOException {
        StructuredRedactionOutput out = spg.redactXml("   ");
        assertEquals("   ", out.getRedactedContent());
        assertEquals(0, out.getMatchCount());
    }

    @Test
    void invalidXml_throwsIOException() {
        assertThrows(IOException.class,
            () -> spg.redactXml("<unclosed>"));
    }

    @Test
    void hasPII_falseWhenNoMatches() throws IOException {
        StructuredRedactionOutput out = spg.redactXml(
            "<status>all clear</status>");
        assertFalse(out.hasPII());
    }

    @Test
    void hasPII_trueWhenMatchFound() throws IOException {
        StructuredRedactionOutput out = spg.redactXml(
            "<msg>Contact user@example.com</msg>");
        assertTrue(out.hasPII());
    }

    @Test
    void toString_containsMatchCount() throws IOException {
        StructuredRedactionOutput out = spg.redactXml(
            "<msg>user@example.com</msg>");
        assertTrue(out.toString().contains("matches=1"));
    }
}

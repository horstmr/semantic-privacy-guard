package com.semanticprivacyguard;

import com.semanticprivacyguard.config.CustomPattern;
import com.semanticprivacyguard.config.SPGConfig;
import com.semanticprivacyguard.model.PIIMatch;
import com.semanticprivacyguard.model.PIIType;
import com.semanticprivacyguard.model.RedactionResult;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.regex.PatternSyntaxException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for custom pattern registry: {@link CustomPattern} and
 * {@link SPGConfig.Builder#addPattern}.
 */
class CustomPatternTest {

    // ── CustomPattern construction ────────────────────────────────────────────

    @Test
    void constructor_storesFields() {
        CustomPattern cp = new CustomPattern(
            PIIType.GENERIC_PII, "EMP-\\d{6}", 0.99, "Employee ID");
        assertEquals(PIIType.GENERIC_PII, cp.getType());
        assertNotNull(cp.getPattern());
        assertEquals(0.99, cp.getConfidence(), 1e-9);
        assertEquals("Employee ID", cp.getDescription());
    }

    @Test
    void constructor_withoutDescription_defaultsToRegex() {
        CustomPattern cp = new CustomPattern(PIIType.GENERIC_PII, "EMP-\\d{6}", 0.95);
        assertEquals("EMP-\\d{6}", cp.getDescription());
    }

    @Test
    void constructor_nullType_throws() {
        assertThrows(NullPointerException.class,
            () -> new CustomPattern(null, "EMP-\\d{6}", 0.99));
    }

    @Test
    void constructor_nullRegex_throws() {
        assertThrows(NullPointerException.class,
            () -> new CustomPattern(PIIType.GENERIC_PII, null, 0.99));
    }

    @Test
    void constructor_invalidRegex_throws() {
        assertThrows(PatternSyntaxException.class,
            () -> new CustomPattern(PIIType.GENERIC_PII, "[invalid", 0.99));
    }

    @Test
    void constructor_confidenceOutOfRange_throws() {
        assertThrows(IllegalArgumentException.class,
            () -> new CustomPattern(PIIType.GENERIC_PII, "EMP-\\d{6}", 0.0));
        assertThrows(IllegalArgumentException.class,
            () -> new CustomPattern(PIIType.GENERIC_PII, "EMP-\\d{6}", 1.1));
    }

    @Test
    void constructor_confidenceBoundary_accepted() {
        assertDoesNotThrow(
            () -> new CustomPattern(PIIType.GENERIC_PII, "EMP-\\d{6}", 1.0));
        assertDoesNotThrow(
            () -> new CustomPattern(PIIType.GENERIC_PII, "EMP-\\d{6}", 0.01));
    }

    // ── SPGConfig.Builder#addPattern ──────────────────────────────────────────

    @Test
    void addPattern_storedInConfig() {
        SPGConfig config = SPGConfig.builder()
            .addPattern(PIIType.GENERIC_PII, "EMP-\\d{6}", 0.99, "Employee ID")
            .build();
        assertEquals(1, config.getCustomPatterns().size());
        assertEquals("Employee ID", config.getCustomPatterns().get(0).getDescription());
    }

    @Test
    void addPattern_multiplePatterns_allStored() {
        SPGConfig config = SPGConfig.builder()
            .addPattern(PIIType.GENERIC_PII, "EMP-\\d{6}", 0.99, "Employee ID")
            .addPattern(PIIType.GENERIC_PII, "POL-[A-Z]{2}-\\d{8}", 0.97, "Policy number")
            .build();
        assertEquals(2, config.getCustomPatterns().size());
    }

    @Test
    void addPattern_listIsUnmodifiable() {
        SPGConfig config = SPGConfig.builder()
            .addPattern(PIIType.GENERIC_PII, "EMP-\\d{6}", 0.99)
            .build();
        assertThrows(UnsupportedOperationException.class,
            () -> config.getCustomPatterns().clear());
    }

    @Test
    void defaultConfig_hasNoCustomPatterns() {
        SPGConfig config = SPGConfig.defaults();
        assertTrue(config.getCustomPatterns().isEmpty());
    }

    // ── End-to-end: custom pattern detected and redacted ──────────────────────

    @Test
    void customPattern_employeeId_detected() {
        SPGConfig config = SPGConfig.builder()
            .addPattern(PIIType.GENERIC_PII, "EMP-\\d{6}", 0.99, "Employee ID")
            .build();
        SemanticPrivacyGuard spg = SemanticPrivacyGuard.create(config);

        RedactionResult result = spg.redact("Task EMP-042731 is pending.");

        assertTrue(result.getRedactedText().contains("[PII_1]"),
            "Employee ID should be tokenised to [PII_1]");
        assertFalse(result.getRedactedText().contains("EMP-042731"),
            "Original employee ID should be removed");
        assertEquals(1, result.getMatchCount());
    }

    @Test
    void customPattern_multipleMatches_numberedSequentially() {
        SPGConfig config = SPGConfig.builder()
            .addPattern(PIIType.GENERIC_PII, "EMP-\\d{6}", 0.99, "Employee ID")
            .build();
        SemanticPrivacyGuard spg = SemanticPrivacyGuard.create(config);

        RedactionResult result = spg.redact(
            "Reviewers: EMP-000001, EMP-000002, EMP-000003.");

        String redacted = result.getRedactedText();
        assertTrue(redacted.contains("[PII_1]"), "First employee ID missing");
        assertTrue(redacted.contains("[PII_2]"), "Second employee ID missing");
        assertTrue(redacted.contains("[PII_3]"), "Third employee ID missing");
        assertEquals(3, result.getMatchCount());
    }

    @Test
    void customPattern_twoDifferentCustomPatterns() {
        SPGConfig config = SPGConfig.builder()
            .addPattern(PIIType.GENERIC_PII, "EMP-\\d{6}", 0.99, "Employee ID")
            .addPattern(PIIType.GENERIC_PII, "POL-[A-Z]{2}-\\d{4}", 0.97, "Policy")
            .build();
        SemanticPrivacyGuard spg = SemanticPrivacyGuard.create(config);

        RedactionResult result = spg.redact(
            "Employee EMP-123456 holds policy POL-GB-9988.");

        assertFalse(result.getRedactedText().contains("EMP-123456"));
        assertFalse(result.getRedactedText().contains("POL-GB-9988"));
        assertEquals(2, result.getMatchCount());
    }

    @Test
    void customPattern_analyse_returnsMatches() {
        SPGConfig config = SPGConfig.builder()
            .addPattern(PIIType.GENERIC_PII, "EMP-\\d{6}", 0.99, "Employee ID")
            .build();
        SemanticPrivacyGuard spg = SemanticPrivacyGuard.create(config);

        List<PIIMatch> matches = spg.analyse("The ticket EMP-555001 is pending.");

        assertEquals(1, matches.size());
        assertEquals("EMP-555001", matches.get(0).getValue());
        assertEquals(PIIType.GENERIC_PII, matches.get(0).getType());
    }

    @Test
    void customPattern_noMatch_returnsCleanText() {
        SPGConfig config = SPGConfig.builder()
            .addPattern(PIIType.GENERIC_PII, "EMP-\\d{6}", 0.99)
            .build();
        SemanticPrivacyGuard spg = SemanticPrivacyGuard.create(config);

        RedactionResult result = spg.redact("Nothing sensitive here.");

        assertEquals("Nothing sensitive here.", result.getRedactedText());
        assertEquals(0, result.getMatchCount());
    }

    @Test
    void toString_includesCustomPatternCount() {
        SPGConfig config = SPGConfig.builder()
            .addPattern(PIIType.GENERIC_PII, "EMP-\\d{6}", 0.99)
            .build();
        assertTrue(config.toString().contains("customPatterns=1"));
    }
}

package com.semanticprivacyguard.config;

import com.semanticprivacyguard.model.PIIType;

import java.util.Objects;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * A caller-supplied regex pattern registered via {@link SPGConfig.Builder#addPattern}
 * to detect organisation-specific identifiers that the built-in heuristics do not cover.
 *
 * <p>Typical uses include employee IDs ({@code EMP-\d{6}}), internal policy
 * numbers ({@code POL-[A-Z]{2}-\d{8}}), patient MRN codes, or any proprietary
 * reference format unique to your systems.</p>
 *
 * <h2>Usage</h2>
 * <pre>{@code
 * SPGConfig config = SPGConfig.builder()
 *     .addPattern(PIIType.GENERIC_PII, "EMP-\\d{6}",       0.99, "Employee ID")
 *     .addPattern(PIIType.GENERIC_PII, "POL-[A-Z]{2}-\\d{8}", 0.97, "Policy number")
 *     .build();
 *
 * SemanticPrivacyGuard spg = SemanticPrivacyGuard.create(config);
 * RedactionResult r = spg.redact("Assigned to EMP-042731, policy POL-GB-00123456.");
 * // → "Assigned to [PII_1], policy [PII_2]."
 * }</pre>
 *
 * <p>Patterns are applied by {@code HeuristicDetector} after all built-in patterns,
 * so built-in matches always take precedence for overlapping spans.</p>
 *
 * @author Hemant Naik
 * @since 1.2.0
 */
public final class CustomPattern {

    private final PIIType type;
    private final Pattern pattern;
    private final double  confidence;
    private final String  description;

    // ── Constructors ──────────────────────────────────────────────────────────

    /**
     * Creates a custom pattern with a human-readable description label.
     *
     * @param type        PII type to assign to matches (e.g. {@link PIIType#GENERIC_PII})
     * @param regex       Java regular expression; must not be {@code null}
     * @param confidence  confidence score for each match, in {@code (0.0, 1.0]}
     * @param description human-readable label used in logs and audit reports;
     *                    defaults to the regex string if {@code null}
     * @throws PatternSyntaxException   if {@code regex} is not a valid Java regex
     * @throws IllegalArgumentException if {@code confidence} is out of range
     */
    public CustomPattern(PIIType type, String regex, double confidence, String description) {
        this.type    = Objects.requireNonNull(type,  "type must not be null");
        this.pattern = Pattern.compile(
                       Objects.requireNonNull(regex, "regex must not be null"));
        if (confidence <= 0.0 || confidence > 1.0) {
            throw new IllegalArgumentException(
                "confidence must be in (0.0, 1.0] but was: " + confidence);
        }
        this.confidence  = confidence;
        this.description = (description != null && !description.isBlank())
                           ? description : regex;
    }

    /**
     * Creates a custom pattern without a description (description defaults to the
     * regex string).
     *
     * @param type       PII type to assign to matches
     * @param regex      Java regular expression
     * @param confidence confidence score in {@code (0.0, 1.0]}
     */
    public CustomPattern(PIIType type, String regex, double confidence) {
        this(type, regex, confidence, null);
    }

    // ── Accessors ─────────────────────────────────────────────────────────────

    /** Returns the PII type that will be assigned to each match. */
    public PIIType getType()       { return type;        }

    /** Returns the compiled {@link Pattern} ready for matching. */
    public Pattern getPattern()    { return pattern;     }

    /** Returns the confidence score assigned to each match ({@code (0.0, 1.0]}). */
    public double  getConfidence() { return confidence;  }

    /** Returns the human-readable description of this pattern. */
    public String  getDescription(){ return description; }

    @Override
    public String toString() {
        return "CustomPattern{type=" + type
             + ", desc='" + description + '\''
             + ", conf=" + confidence + '}';
    }
}

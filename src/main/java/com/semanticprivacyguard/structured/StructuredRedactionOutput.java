package com.semanticprivacyguard.structured;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

/**
 * Immutable result of a structured-document (JSON or XML) redaction operation.
 *
 * <p>Contains:</p>
 * <ul>
 *   <li>The redacted document as a string (JSON or XML)</li>
 *   <li>A token-to-original reverse map enabling de-tokenisation after an LLM
 *       response is received</li>
 *   <li>The total count of PII matches found across the whole document</li>
 * </ul>
 *
 * <h2>De-tokenisation example</h2>
 * <pre>{@code
 * StructuredRedactionOutput out = spg.redactJson(jsonString);
 *
 * // Send out.getRedactedContent() to the LLM — it never sees real PII.
 *
 * // After the LLM responds, restore original values:
 * String llmResponse = callLlm(out.getRedactedContent());
 * for (Map.Entry<String, String> e : out.getReverseMap().entrySet()) {
 *     llmResponse = llmResponse.replace(e.getKey(), e.getValue());
 * }
 * }</pre>
 *
 * @author Hemant Naik
 * @since 1.2.0
 */
public final class StructuredRedactionOutput {

    private final String              redactedContent;
    private final Map<String, String> reverseMap;
    private final int                 matchCount;

    /**
     * @param redactedContent the full redacted document string
     * @param reverseMap      token → original value map (may be empty)
     * @param matchCount      total PII matches detected across the document
     */
    public StructuredRedactionOutput(String redactedContent,
                                     Map<String, String> reverseMap,
                                     int matchCount) {
        this.redactedContent = Objects.requireNonNull(redactedContent,
                                   "redactedContent must not be null");
        this.reverseMap      = Collections.unmodifiableMap(
                               Objects.requireNonNull(reverseMap,
                                   "reverseMap must not be null"));
        this.matchCount      = matchCount;
    }

    // ── Accessors ─────────────────────────────────────────────────────────────

    /**
     * Returns the full redacted document as a string.
     * Structure (JSON/XML) is preserved; only string values that contained
     * PII are modified.
     */
    public String getRedactedContent()         { return redactedContent; }

    /**
     * Returns the token-to-original-value map, enabling de-tokenisation after
     * receiving an LLM response.  Empty if
     * {@link com.semanticprivacyguard.config.SPGConfig#isBuildReverseMap()} is
     * {@code false}.
     */
    public Map<String, String> getReverseMap() { return reverseMap;      }

    /** Returns the total number of PII matches found across the document. */
    public int  getMatchCount()                { return matchCount;      }

    /** Returns {@code true} if at least one PII match was detected. */
    public boolean hasPII()                    { return matchCount > 0;  }

    @Override
    public String toString() {
        return String.format(
            "StructuredRedactionOutput{matches=%d, reverseMapEntries=%d}",
            matchCount, reverseMap.size());
    }
}

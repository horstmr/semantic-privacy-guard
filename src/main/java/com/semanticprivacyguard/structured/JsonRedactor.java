package com.semanticprivacyguard.structured;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

import com.semanticprivacyguard.config.SPGConfig;
import com.semanticprivacyguard.detector.CompositeDetector;
import com.semanticprivacyguard.model.PIIMatch;
import com.semanticprivacyguard.model.PIIType;
import com.semanticprivacyguard.tokenizer.PIITokenizer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Redacts PII from JSON documents while fully preserving document structure.
 *
 * <p>The redactor recursively walks every node in the JSON tree. String values
 * are scanned with the full SPG detection pipeline (Heuristic + ML + optional NLP);
 * numbers, booleans, and nulls are passed through untouched.  Token counters are
 * document-scoped, so {@code [EMAIL_1]} in one field and {@code [EMAIL_2]} in
 * another are globally unique across the whole document — enabling accurate
 * de-tokenisation after an LLM response.</p>
 *
 * <h2>Prerequisites</h2>
 * <p>{@code com.fasterxml.jackson.core:jackson-databind} must be on the runtime
 * classpath.  It is declared {@code optional} in SPG's POM so it is not pulled
 * in transitively; add it explicitly to your own project:</p>
 * <pre>{@code
 * <dependency>
 *   <groupId>com.fasterxml.jackson.core</groupId>
 *   <artifactId>jackson-databind</artifactId>
 *   <version>2.17.0</version>
 * </dependency>
 * }</pre>
 *
 * <h2>Usage via the main API (recommended)</h2>
 * <pre>{@code
 * SemanticPrivacyGuard spg = SemanticPrivacyGuard.create();
 * StructuredRedactionOutput out = spg.redactJson(jsonString);
 *
 * String clean = out.getRedactedContent();  // structure-preserved JSON
 * int    hits  = out.getMatchCount();
 * }</pre>
 *
 * @author Hemant Naik
 * @since 1.2.0
 */
public final class JsonRedactor {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final CompositeDetector detector;
    private final PIITokenizer      tokenizer;
    private final SPGConfig         config;

    /**
     * @param detector  the composite detection pipeline
     * @param tokenizer the tokenizer (mode is driven by {@code config})
     * @param config    active SPG configuration
     */
    public JsonRedactor(CompositeDetector detector,
                        PIITokenizer      tokenizer,
                        SPGConfig         config) {
        this.detector  = detector;
        this.tokenizer = tokenizer;
        this.config    = config;
    }

    // ── Public API ────────────────────────────────────────────────────────────

    /**
     * Parses {@code json}, redacts all string values that contain PII, and
     * returns the sanitised JSON with the same structure.
     *
     * @param json a valid JSON string; may be an object, array, or scalar
     * @return a {@link StructuredRedactionOutput} containing the redacted JSON,
     *         a reverse map, and the total match count
     * @throws IOException if {@code json} is not valid JSON
     */
    public StructuredRedactionOutput redact(String json) throws IOException {
        if (json == null || json.isBlank()) {
            return new StructuredRedactionOutput(
                json == null ? "" : json, Map.of(), 0);
        }

        JsonNode root = MAPPER.readTree(json);

        // Document-scoped token counters — shared across the entire tree walk
        Map<PIIType, Integer> globalCounters = new EnumMap<>(PIIType.class);
        Map<String, String>   reverseMap     = new LinkedHashMap<>();
        int[]                 totalMatches   = {0};   // mutable int inside lambda-safe array

        JsonNode redacted = redactNode(root, globalCounters, reverseMap, totalMatches);

        String output = MAPPER.writeValueAsString(redacted);
        return new StructuredRedactionOutput(
            output,
            config.isBuildReverseMap() ? reverseMap : Map.of(),
            totalMatches[0]);
    }

    // ── Private tree-walk ─────────────────────────────────────────────────────

    private JsonNode redactNode(JsonNode              node,
                                Map<PIIType, Integer> counters,
                                Map<String, String>   reverseMap,
                                int[]                 totalMatches) {
        if (node.isTextual()) {
            return redactTextNode(node.textValue(), counters, reverseMap, totalMatches);

        } else if (node.isObject()) {
            ObjectNode result = JsonNodeFactory.instance.objectNode();
            node.fields().forEachRemaining(entry ->
                result.set(entry.getKey(),
                           redactNode(entry.getValue(), counters, reverseMap, totalMatches)));
            return result;

        } else if (node.isArray()) {
            ArrayNode result = JsonNodeFactory.instance.arrayNode();
            node.forEach(child ->
                result.add(redactNode(child, counters, reverseMap, totalMatches)));
            return result;
        }

        // numbers, booleans, nulls — pass through unchanged
        return node;
    }

    private JsonNode redactTextNode(String                text,
                                    Map<PIIType, Integer>  counters,
                                    Map<String, String>    reverseMap,
                                    int[]                  totalMatches) {
        int minSev = config.getMinimumSeverity();

        List<PIIMatch> all      = detector.detect(text);
        List<PIIMatch> filtered = new ArrayList<>(all.size());
        for (PIIMatch m : all) {
            if (m.getType().getSeverity() >= minSev) filtered.add(m);
        }

        if (filtered.isEmpty()) return TextNode.valueOf(text);

        PIITokenizer.RedactionOutput out = tokenizer.redact(text, filtered, counters);
        totalMatches[0] += filtered.size();
        reverseMap.putAll(out.reverseMap());
        return TextNode.valueOf(out.redactedText());
    }
}

package com.semanticprivacyguard.structured;

import com.semanticprivacyguard.config.SPGConfig;
import com.semanticprivacyguard.detector.CompositeDetector;
import com.semanticprivacyguard.model.PIIMatch;
import com.semanticprivacyguard.model.PIIType;
import com.semanticprivacyguard.tokenizer.PIITokenizer;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Redacts PII from XML documents while fully preserving document structure.
 *
 * <p>Walks all text nodes and CDATA sections in the DOM, applying the full SPG
 * detection pipeline to each.  Attribute values are also scanned.  Token
 * counters are document-scoped so numbering is globally unique across the whole
 * document, enabling accurate de-tokenisation.</p>
 *
 * <p><strong>No extra dependencies required</strong> — uses {@code javax.xml}
 * which is bundled with the JDK.  XXE protections are enabled by default.</p>
 *
 * <h2>Usage via the main API (recommended)</h2>
 * <pre>{@code
 * SemanticPrivacyGuard spg = SemanticPrivacyGuard.create();
 * StructuredRedactionOutput out = spg.redactXml(xmlString);
 *
 * String clean = out.getRedactedContent();  // structure-preserved XML
 * int    hits  = out.getMatchCount();
 * }</pre>
 *
 * @author Hemant Naik
 * @since 1.2.0
 */
public final class XmlRedactor {

    private final CompositeDetector detector;
    private final PIITokenizer      tokenizer;
    private final SPGConfig         config;

    /**
     * @param detector  the composite detection pipeline
     * @param tokenizer the tokenizer (mode is driven by {@code config})
     * @param config    active SPG configuration
     */
    public XmlRedactor(CompositeDetector detector,
                       PIITokenizer      tokenizer,
                       SPGConfig         config) {
        this.detector  = detector;
        this.tokenizer = tokenizer;
        this.config    = config;
    }

    // ── Public API ────────────────────────────────────────────────────────────

    /**
     * Parses {@code xml}, redacts all text content and attribute values that
     * contain PII, then serializes the modified DOM back to a string.
     *
     * @param xml a well-formed XML string
     * @return a {@link StructuredRedactionOutput} containing the redacted XML,
     *         a reverse map, and the total match count
     * @throws IOException if {@code xml} is not well-formed or an I/O error occurs
     */
    public StructuredRedactionOutput redact(String xml) throws IOException {
        if (xml == null || xml.isBlank()) {
            return new StructuredRedactionOutput(
                xml == null ? "" : xml, Map.of(), 0);
        }

        try {
            Document doc = parse(xml);

            // Document-scoped token counters — shared across the entire DOM walk
            Map<PIIType, Integer> globalCounters = new EnumMap<>(PIIType.class);
            Map<String, String>   reverseMap     = new LinkedHashMap<>();
            int[]                 totalMatches   = {0};

            redactNode(doc, globalCounters, reverseMap, totalMatches);

            String output = serialize(doc);
            return new StructuredRedactionOutput(
                output,
                config.isBuildReverseMap() ? reverseMap : Map.of(),
                totalMatches[0]);

        } catch (ParserConfigurationException | SAXException | TransformerException e) {
            throw new IOException(
                "Failed to process XML: " + e.getMessage(), e);
        }
    }

    // ── Private DOM-walk ──────────────────────────────────────────────────────

    private void redactNode(Node                  node,
                            Map<PIIType, Integer>  counters,
                            Map<String, String>    reverseMap,
                            int[]                  totalMatches) {
        int type   = node.getNodeType();
        int minSev = config.getMinimumSeverity();

        // Redact text nodes and CDATA sections
        if (type == Node.TEXT_NODE || type == Node.CDATA_SECTION_NODE) {
            String text = node.getNodeValue();
            if (text != null && !text.isBlank()) {
                List<PIIMatch> filtered = filterBySeverity(text, minSev);
                if (!filtered.isEmpty()) {
                    PIITokenizer.RedactionOutput out =
                        tokenizer.redact(text, filtered, counters);
                    node.setNodeValue(out.redactedText());
                    totalMatches[0] += filtered.size();
                    reverseMap.putAll(out.reverseMap());
                }
            }
        }

        // Redact element attribute values
        NamedNodeMap attrs = node.getAttributes();
        if (attrs != null) {
            for (int i = 0; i < attrs.getLength(); i++) {
                Node attr = attrs.item(i);
                String val = attr.getNodeValue();
                if (val != null && !val.isBlank()) {
                    List<PIIMatch> filtered = filterBySeverity(val, minSev);
                    if (!filtered.isEmpty()) {
                        PIITokenizer.RedactionOutput out =
                            tokenizer.redact(val, filtered, counters);
                        attr.setNodeValue(out.redactedText());
                        totalMatches[0] += filtered.size();
                        reverseMap.putAll(out.reverseMap());
                    }
                }
            }
        }

        // Recurse into child nodes
        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            redactNode(children.item(i), counters, reverseMap, totalMatches);
        }
    }

    private List<PIIMatch> filterBySeverity(String text, int minSev) {
        List<PIIMatch> all      = detector.detect(text);
        List<PIIMatch> filtered = new ArrayList<>(all.size());
        for (PIIMatch m : all) {
            if (m.getType().getSeverity() >= minSev) filtered.add(m);
        }
        return filtered;
    }

    // ── XML parse / serialize helpers ─────────────────────────────────────────

    private Document parse(String xml)
            throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        // XXE hardening — must be set before newDocumentBuilder()
        factory.setFeature(
            "http://apache.org/xml/features/disallow-doctype-decl", true);
        factory.setFeature(
            "http://xml.org/sax/features/external-general-entities", false);
        factory.setFeature(
            "http://xml.org/sax/features/external-parameter-entities", false);
        factory.setExpandEntityReferences(false);

        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(new InputSource(new StringReader(xml)));
    }

    private String serialize(Document doc) throws TransformerException {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer t = tf.newTransformer();
        t.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        t.setOutputProperty(OutputKeys.INDENT, "no");
        StringWriter sw = new StringWriter();
        t.transform(new DOMSource(doc), new StreamResult(sw));
        return sw.toString();
    }
}

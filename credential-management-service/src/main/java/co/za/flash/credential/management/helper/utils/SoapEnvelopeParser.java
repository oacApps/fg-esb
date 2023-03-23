package co.za.flash.credential.management.helper.utils;

import co.za.flash.credential.management.helper.enums.SoapEnvFieldNames;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SoapEnvelopeParser {
    public static String maskPersonalInfo(String originalString, Set<String> maskKeys, String maskingValue) {

        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer;
        try {
            String bodyFieldName = SoapEnvFieldNames.Body.fieldName;
            //Get Document Builder
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            //Build Document
            Document document = builder.parse(new InputSource(new StringReader(originalString)));
            //Normalize the XML Structure; It's just too important !!
            document.getDocumentElement().normalize();

            //Here comes the root node
            Element root = document.getDocumentElement();
            if (root.hasChildNodes()) {
                NodeList nList = root.getChildNodes();
                for (int i = 0; i < nList.getLength(); i++ ) {
                    Node bodyNode = nList.item(i);
                    if (bodyNode.hasChildNodes() && bodyNode.getNodeName() == bodyFieldName) {
                        maskChildElements(bodyNode, maskKeys, maskingValue);
                    }
                }
            }
            transformer = tf.newTransformer();
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(document), new StreamResult(writer));
            String output = writer.getBuffer().toString();
            return output;
        } catch (Exception e) {
            // log it?
            e.getLocalizedMessage();
        }
        return originalString;
    }

    public static String appendEnvelopeBodyWrapperTop() {
        StringBuilder sb = new StringBuilder();
        sb.append(SoapEnvFieldNames.Envelop.getKeyFieldLeft());
        sb.append(SoapEnvFieldNames.Header.getEmptyKeyField());
        sb.append(SoapEnvFieldNames.Body.getKeyFieldLeft());
        return sb.toString();
    }
    public static  String appendEnvelopeBodyWrapperBottom() {
        StringBuilder sb = new StringBuilder();
        sb.append(SoapEnvFieldNames.Body.getKeyFieldRight());
        sb.append(SoapEnvFieldNames.Envelop.getKeyFieldRight());
        return sb.toString();
    }

    private static Node getBodyNode(String rawResponse) {
        if (StringUtil.isNullOrBlank(rawResponse))
            return null;
        Node resultNode = null;
        try {
            String bodyFieldName = SoapEnvFieldNames.Body.fieldName;
            //Get Document Builder
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            //Build Document
            Document document = builder.parse(new InputSource(new StringReader(rawResponse)));
            //Normalize the XML Structure; It's just too important !!
            document.getDocumentElement().normalize();

            //Here comes the root node
            Element root = document.getDocumentElement();
            if (root.hasChildNodes()) {
                NodeList nList = root.getChildNodes();
                for (int i = 0; i < nList.getLength(); i++ ) {
                    Node bodyNode = nList.item(i);
                    if (bodyNode.hasChildNodes() && bodyNode.getNodeName() == bodyFieldName) {
                        resultNode = bodyNode;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            // log it?
            e.getLocalizedMessage();
        }
        return resultNode;
    }

    private static void maskChildElements(Node node, Set<String> maskKeys, String maskingValue) {
        if (node != null) {
            if (node.getNodeType() == Node.ELEMENT_NODE && maskKeys.contains(node.getNodeName())) {
                Element eElement = (Element) node;
                eElement.getFirstChild().setTextContent(maskingValue);
            } else if (node.hasChildNodes()) {
                NodeList list = node.getChildNodes();
                for (int i = 0; i < list.getLength(); i++) {
                    maskChildElements(list.item(i), maskKeys, maskingValue);
                }
            }
        }
    }

    public static Node getNodeFromBody(String rawResponse, String nodeTagName) {
        if (StringUtil.isNullOrBlank(rawResponse))
            return null;
        Node resultNode = getChildNode(getBodyNode(rawResponse), nodeTagName);
        return resultNode;
    }

    public static Node getChildNode(Node parentNode, String tagName)  {
        if (parentNode != null && parentNode.getNodeType() == Node.ELEMENT_NODE) {
            Element eElement = (Element) parentNode;
            NodeList nodes = eElement.getElementsByTagName(tagName);
            if (nodes != null && nodes.getLength() > 0) {
                return nodes.item(0);
            }
        }
        return null;
    }

    public static List<Node> getChildNodes(Node parentNode, String tagName)  {
        ArrayList<Node> result = new ArrayList<>();
        if (parentNode != null && parentNode.getNodeType() == Node.ELEMENT_NODE) {
            Element eElement = (Element) parentNode;
            NodeList nodes = eElement.getElementsByTagName(tagName);
            if (nodes != null && nodes.getLength() > 0) {
                for (int i = 0; i < nodes.getLength(); i++) {
                    result.add(nodes.item(i));
                }
            }
        }
        return result;
    }

    public static String parseString(Node parentNode, String tagName, String defaultValue)  {
        if (parentNode.getNodeType() == Node.ELEMENT_NODE) {
            Element eElement = (Element) parentNode;
            NodeList nodes = eElement.getElementsByTagName(tagName);
            if (nodes != null && nodes.getLength() > 0) {
                return nodes.item(0).getTextContent();
            }
        }
        return defaultValue;
    }

    public static int parseInt(Node parentNode, String tagName, int defaultValue)  {
        if (parentNode.getNodeType() == Node.ELEMENT_NODE) {
            Element eElement = (Element) parentNode;
            NodeList nodes = eElement.getElementsByTagName(tagName);
            if (nodes != null && nodes.getLength() > 0) {
                String valueStr = nodes.item(0).getTextContent();
                return Integer.parseInt(valueStr);
            }
        }
        return defaultValue;
    }

    public static boolean parseBoolean(Node parentNode, String tagName, boolean defaultValue)  {
        if (parentNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) parentNode;
            NodeList nodes = eElement.getElementsByTagName(tagName);
            if (nodes != null && nodes.getLength() > 0) {
                String valueStr = nodes.item(0).getTextContent();
                return Boolean.parseBoolean(valueStr);
            }
        }
        return defaultValue;
    }
}

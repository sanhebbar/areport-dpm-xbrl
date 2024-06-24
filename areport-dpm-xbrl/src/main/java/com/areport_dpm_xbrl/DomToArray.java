package com.areport_dpm_xbrl;

import com.sun.xml.internal.txw2.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import javax.lang.model.element.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.Node;
import java.io.File;
import java.util.*;

public class DomToArray {
    public static Document invoke(String path) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document dom = builder.parse(new File(path));
            return dom;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Map<String, Object> getArray(String path) {
        Document dom = invoke(path);
        Element root = dom.getDocumentElement();
        Map<String, Object> output = domNodeToArray(root);
        output.put("@root", root.getTagName());
        return output;
    }

    private static Map<String, Object> domNodeToArray(Node node) {
        Map<String, Object> output = new HashMap<>();
        switch (node.getNodeType()) {
            case Node.CDATA_SECTION_NODE:
            case Node.TEXT_NODE:
                return Map.of("@content", node.getTextContent().trim());
            case Node.ELEMENT_NODE:
                NodeList childNodes = node.getChildNodes();
                for (int i = 0; i < childNodes.getLength(); i++) {
                    Node child = childNodes.item(i);
                    Map<String, Object> childOutput = domNodeToArray(child);
                    if (child.hasChildNodes()) {
                        String tagName = child.getNodeName();
                        output.computeIfAbsent(tagName, k -> new ArrayList<>());
                        ((ArrayList<Object>) output.get(tagName)).add(childOutput);
                    } else if (childOutput.containsKey("@content")) {
                        output.put(node.getNodeName(), childOutput.get("@content"));
                    }
                }
                NamedNodeMap attributes = node.getAttributes();
                if (attributes.getLength() > 0) {
                    Map<String, String> attributeMap = new HashMap<>();
                    for (int i = 0; i < attributes.getLength(); i++) {
                        Node attr = attributes.item(i);
                        attributeMap.put(attr.getNodeName(), attr.getNodeValue());
                    }
                    output.put("@attributes", attributeMap);
                }
                break;
        }
        return output;
    }

    public static Map<String, Object> searchMultiDim(List<Map<String, Object>> arr, String field, String value) {
        Map<String, Object> found = new HashMap<>();
        for (Map<String, Object> row : arr) {
            if (row.containsKey(field) && row.get(field).equals(value)) {
                found.putAll(row);
            }
        }
        return found;
    }

    public static Object searchMultiDimMultiVal(List<Map<String, Object>> arr, String value, String role) {
        List<Map<String, Object>> foundList = new ArrayList<>();
        for (Map<String, Object> element : arr) {
            if (element.containsKey("role") && element.get("role").equals(role)) {
                foundList.add(element);
            }
        }

        if (!arr.isEmpty()) {
            if ("http://www.eba.europa.eu/xbrl/role/dpm-db-id".equals(role)) {
                Map<String, Object> found = searchMultiDim(foundList, "from", value);
                if (found != null) {
                    return found.get("@content");
                }
            }
        }
        return null;
    }

    public static List<String> getPath(String path, List<String> stringList, String returnValue) {
        List<String> dir = new ArrayList<>();
        try {
            File folder = new File(path);
            List<String> extensions = Arrays.asList("xsd");
            File[] listOfFiles = folder.listFiles();

            if (listOfFiles != null) {
                for (File file : listOfFiles) {
                    if (file.isDirectory()) {
                        continue;
                    }
                    String content = file.getPath();
                    for (String str : stringList) {
                        if (content.contains(str)) {
                            String extension = content.substring(content.lastIndexOf(".") + 1);
                            if (extensions.contains(extension)) {
                                if (returnValue == null) {
                                    dir.add(content);
                                } else {
                                    return Collections.singletonList(content);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dir;
    }

    public static String buildUrl(Map<String, String> parts) {
        String scheme = parts.getOrDefault("scheme", "") + "://";
        String host = Config.owner;
        String port = parts.getOrDefault("port", "");
        String user = parts.getOrDefault("user", "");
        String path = parts.getOrDefault("path", "");
        String query = parts.getOrDefault("query", "");
        String fragment = parts.getOrDefault("fragment", "");

        return scheme + user + host + port + path + query + fragment;
    }

    public static int strposArr(String haystack, String[] needles) {
        for (String needle : needles) {
            int pos = haystack.indexOf(needle);
            if (pos != -1) {
                return pos;
            }
        }
        return -1;
    }

    public static List<Object> multidimensionalArrToSingle(List<Object> array) {
        List<Object> result = new ArrayList<>();
        for (Object value : array) {
            if (value instanceof List) {
                result.addAll(multidimensionalArrToSingle((List<Object>) value));
            } else {
                result.add(value);
            }
        }
        return result;
    }
}

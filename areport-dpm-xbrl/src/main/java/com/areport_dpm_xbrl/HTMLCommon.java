package com.areport_dpm_xbrl;

import java.util.HashMap;
import java.util.Map;

public abstract class HTMLCommon {

    // Associative array of attributes
    private Map<String, String> attributes = new HashMap<>();

    // Tab offset of the tag
    private int tabOffset = 0;

    // Tab string
    private String tab = "\t";

    // Contains the line end string
    private String lineEnd = "\n";

    // HTML comment on the object
    private String comment = "";

    // Class constructor
    public HTMLCommon(Map<String, String> attributes, int tabOffset) {
        setAttributes(attributes);
        setTabOffset(tabOffset);
    }

    // Returns the current API version
    public double apiVersion() {
        return 1.7;
    }

    // Returns the lineEnd
    private String getLineEnd() {
        return lineEnd;
    }

    // Returns a string containing the unit for indenting HTML
    private String getTab() {
        return tab;
    }

    // Returns a string containing the offset for the whole HTML code
    private String getTabs() {
        StringBuilder tabs = new StringBuilder();
        for (int i = 0; i < tabOffset; i++) {
            tabs.append(tab);
        }
        return tabs.toString();
    }

    // Returns an HTML formatted attribute string
    private String getAttrString(Map<String, String> attributes) {
        StringBuilder strAttr = new StringBuilder();
        if (attributes != null) {
            String charset = HTMLCommon.charset();
            for (Map.Entry<String, String> entry : attributes.entrySet()) {
                strAttr.append(' ').append(entry.getKey()).append("=\"")
                        .append(entry.getValue().replace("\"", "&quot;")).append("\"");
            }
        }
        return strAttr.toString();
    }

    // Returns a valid attributes array from either a string or array
    private Map<String, String> parseAttributes(Object attributes) {
        if (attributes instanceof Map) {
            Map<String, String> ret = new HashMap<>();
            ((Map<?, ?>) attributes).forEach((key, value) -> {
                if (key instanceof Integer) {
                    String keyStr = value.toString().toLowerCase();
                    ret.put(keyStr, keyStr);
                } else {
                    ret.put(key.toString().toLowerCase(), value.toString());
                }
            });
            return ret;
        } else if (attributes instanceof String) {
            String attrStr = (String) attributes;
            Map<String, String> arrAttr = new HashMap<>();
            String regex = "(([A-Za-z_:]|[^\\x00-\\x7F])([A-Za-z0-9_:.-]|[^\\x00-\\x7F])*)" +
                    "([ \\n\\t\\r]+)?(=([ \\n\\t\\r]+)?(\"[^\"]*\"|'[^']*'|[^ \\n\\t\\r]*))?";
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex);
            java.util.regex.Matcher matcher = pattern.matcher(attrStr);
            while (matcher.find()) {
                String name = matcher.group(1);
                String value = matcher.group(7);
                if (name.trim().equals(matcher.group(0).trim())) {
                    arrAttr.put(name.toLowerCase().trim(), name.toLowerCase().trim());
                } else {
                    if (value != null && (value.startsWith("\"") || value.startsWith("'"))) {
                        arrAttr.put(name.toLowerCase().trim(), value.substring(1, value.length() - 1));
                    } else {
                        arrAttr.put(name.toLowerCase().trim(), value != null ? value.trim() : "");
                    }
                }
            }
            return arrAttr;
        }
        return new HashMap<>();
    }

    // Updates the attributes in attr1 with the values in attr2 without changing the other existing attributes
    private void updateAttrArray(Map<String, String> attr1, Map<String, String> attr2) {
        if (attr2 != null) {
            attr1.putAll(attr2);
        }
    }

    // Removes the given attribute from the given array
    private void removeAttr(String attr, Map<String, String> attributes) {
        attributes.remove(attr.toLowerCase());
    }

    // Returns the value of the given attribute
    public String getAttribute(String attr) {
        return attributes.get(attr.toLowerCase());
    }

    // Sets the value of the attribute
    public void setAttribute(String name, String value) {
        attributes.put(name.toLowerCase(), value != null ? value : name.toLowerCase());
    }

    // Sets the HTML attributes
    public void setAttributes(Map<String, String> attributes) {
        this.attributes = parseAttributes(attributes);
    }

    // Returns the assoc array (default) or string of attributes
    public Object getAttributes(boolean asString) {
        if (asString) {
            return getAttrString(attributes);
        } else {
            return attributes;
        }
    }

    // Updates the passed attributes without changing the other existing attributes
    public void updateAttributes(Map<String, String> attributes) {
        updateAttrArray(this.attributes, parseAttributes(attributes));
    }

    // Removes an attribute
    public void removeAttribute(String attr) {
        removeAttr(attr, this.attributes);
    }

    // Sets the line end style to Windows, Mac, Unix or a custom string.
    public void setLineEnd(String style) {
        switch (style) {
            case "win":
                this.lineEnd = "\r\n";
                break;
            case "unix":
                this.lineEnd = "\n";
                break;
            case "mac":
                this.lineEnd = "\r";
                break;
            default:
                this.lineEnd = style;
        }
    }

    // Sets the tab offset
    public void setTabOffset(int offset) {
        this.tabOffset = offset;
    }

    // Returns the tabOffset
    public int getTabOffset() {
        return tabOffset;
    }

    // Sets the string used to indent HTML
    public void setTab(String string) {
        this.tab = string;
    }

    // Sets the HTML comment to be displayed at the beginning of the HTML string
    public void setComment(String comment) {
        this.comment = comment;
    }

    // Returns the HTML comment
    public String getComment() {
        return comment;
    }

    // Abstract method. Must be extended to return the object's HTML
    public abstract String toHtml();

    // Displays the HTML to the screen
    public void display() {
        System.out.print(toHtml());
    }

    // Sets the charset to use by escapeHtml function
    private static String charset = "ISO-8859-1";

    public static String charset(String newCharset) {
        if (newCharset != null) {
            charset = newCharset;
        }
        return charset;
    }

    public static String charset() {
        return charset;
    }
}

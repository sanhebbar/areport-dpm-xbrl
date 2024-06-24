package com.areport_dpm_xbrl;

import java.util.HashMap;
import java.util.Map;

public class HTMLTableStorage extends HTMLCommon{
    private String autoFill = "&nbsp;";
    private boolean autoGrow = true;
    private Map<Integer, Map<Integer, Cell>> structure = new HashMap<>();
    private int rows = 0;
    private int cols = 0;
    private int nestLevel = 0;
    private boolean useTGroups = false;

    public HTMLTableStorage(int tabOffset, boolean useTGroups) {
        super(null, tabOffset);
        this.useTGroups = useTGroups;
    }

    public HTMLTableStorage() {
        this(0, false);
    }

    public void setUseTGroups(boolean useTGroups) {
        this.useTGroups = useTGroups;
    }

    public boolean getUseTGroups() {
        return useTGroups;
    }

    public void setAutoFill(String fill) {
        this.autoFill = fill;
    }

    public String getAutoFill() {
        return autoFill;
    }

    public void setAutoGrow(boolean grow) {
        this.autoGrow = grow;
    }

    public boolean getAutoGrow() {
        return autoGrow;
    }

    public void setRowCount(int rows) {
        this.rows = rows;
    }

    public void setColCount(int cols) {
        this.cols = cols;
    }

    public int getRowCount() {
        return rows;
    }

    public int getColCount(Integer row) {
        if (row != null) {
            int count = 0;
            for (Cell cell : structure.get(row).values()) {
                if (cell != null) {
                    count++;
                }
            }
            return count;
        }
        return cols;
    }

    public int getColCount() {
        return cols;
    }

    public void setRowType(int row, String type) {
        for (int col = 0; col < cols; col++) {
            structure.get(row).get(col).setType(type);
        }
    }

    public void setColType(int col, String type) {
        for (int row = 0; row < rows; row++) {
            structure.get(row).get(col).setType(type);
        }
    }

    public void setCellAttributes(int row, int col, Map<String, String> attributes) {
        if (structure.containsKey(row) && structure.get(row).containsKey(col) && "__SPANNED__".equals(structure.get(row).get(col).getContents())) {
            return;
        }
        adjustEnds(row, col, "setCellAttributes", attributes);
        structure.get(row).get(col).setAttributes(attributes);
        updateSpanGrid(row, col);
    }

    public void updateCellAttributes(int row, int col, Map<String, String> attributes) {
        if (structure.containsKey(row) && structure.get(row).containsKey(col) && "__SPANNED__".equals(structure.get(row).get(col).getContents())) {
            return;
        }
        adjustEnds(row, col, "updateCellAttributes", attributes);
        structure.get(row).get(col).updateAttributes(attributes);
        updateSpanGrid(row, col);
    }

    public Map<String, String> getCellAttributes(int row, int col) {
        if (structure.containsKey(row) && structure.get(row).containsKey(col) && !"__SPANNED__".equals(structure.get(row).get(col).getContents())) {
            return structure.get(row).get(col).getAttributes();
        }
        throw new IllegalArgumentException("Invalid table cell reference[" + row + "][" + col + "] in HTMLTableStorage.getCellAttributes");
    }

    public void setCellContents(int row, int col, Object contents, String type) {
        if (contents instanceof Object[]) {
            for (Object content : (Object[]) contents) {
                setSingleCellContents(row, col++, content, type);
            }
        } else {
            setSingleCellContents(row, col, contents, type);
        }
    }

    private void setSingleCellContents(int row, int col, Object contents, String type) {
        if (structure.containsKey(row) && structure.get(row).containsKey(col) && "__SPANNED__".equals(structure.get(row).get(col).getContents())) {
            return;
        }
        adjustEnds(row, col, "setCellContents");
        structure.get(row).get(col).setContents(contents);
        structure.get(row).get(col).setType(type);
    }

    public Object getCellContents(int row, int col) {
        if (structure.containsKey(row) && structure.get(row).containsKey(col) && "__SPANNED__".equals(structure.get(row).get(col).getContents())) {
            return null;
        }
        if (!structure.containsKey(row) || !structure.get(row).containsKey(col)) {
            throw new IllegalArgumentException("Invalid table cell reference[" + row + "][" + col + "] in HTMLTableStorage.getCellContents");
        }
        return structure.get(row).get(col).getContents();
    }

    public void setHeaderContents(int row, int col, Object contents, Map<String, String> attributes) {
        setCellContents(row, col, contents, "TH");
        if (attributes != null) {
            updateCellAttributes(row, col, attributes);
        }
    }

    public int addRow(Object[] contents, Map<String, String> attributes, String type, boolean inTR) {
        if (contents == null) {
            contents = new Object[0];
        }
        int row = rows++;
        for (int col = 0; col < contents.length; col++) {
            if ("td".equals(type)) {
                setCellContents(row, col, contents[col], "");
            } else if ("th".equals(type)) {
                setHeaderContents(row, col, contents[col], new HashMap<>());
            }
        }
        setRowAttributes(row, attributes, inTR);
        return row;
    }

    public void setRowAttributes(int row, Map<String, String> attributes, boolean inTR) {
        if (!inTR) {
            boolean multiAttr = isAttributesArray(attributes);
            for (int col = 0; col < cols; col++) {
                if (multiAttr) {
                    setCellAttributes(row, col, attributes);
                } else {
                    setCellAttributes(row, col, attributes);
                }
            }
        } else {
            adjustEnds(row, 0, "setRowAttributes", attributes);
            structure.get(row).put(-1, new Cell(attributes));
        }
    }

    public void updateRowAttributes(int row, Map<String, String> attributes, boolean inTR) {
        if (!inTR) {
            boolean multiAttr = isAttributesArray(attributes);
            for (int col = 0; col < cols; col++) {
                if (multiAttr) {
                    updateCellAttributes(row, col, attributes);
                } else {
                    updateCellAttributes(row, col, attributes);
                }
            }
        } else {
            adjustEnds(row, 0, "updateRowAttributes", attributes);
            structure.get(row).put(-1, new Cell(attributes));
        }
    }

    public Map<String, String> getRowAttributes(int row) {
        return structure.containsKey(row) && structure.get(row).containsKey(-1) ? structure.get(row).get(-1).getAttributes() : null;
    }

    private boolean isAttributesArray(Map<String, String> attributes) {
        // Implement logic to determine if attributes is an array
        return false;
    }

    private void adjustEnds(int row, int col, String method, Map<String, String> attributes) {
        // Implement logic to adjust ends
    }

    private void adjustEnds(int row, int col, String method) {
        // Implement logic to adjust ends
    }

    private void updateSpanGrid(int row, int col) {
        // Implement logic to update span grid
    }

    @Override
    public String toHtml() {
        return "";
    }

    static class Cell {
        private Map<String, String> attributes;
        private Object contents;
        private String type;

        public Cell() {
            this.attributes = new HashMap<>();
        }

        public Cell(Map<String, String> attributes) {
            this.attributes = attributes;
        }

        public Map<String, String> getAttributes() {
            return attributes;
        }

        public void setAttributes(Map<String, String> attributes) {
            this.attributes = attributes;
        }

        public void updateAttributes(Map<String, String> newAttributes) {
            this.attributes.putAll(newAttributes);
        }

        public Object getContents() {
            return contents;
        }

        public void setContents(Object contents) {
            this.contents = contents;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}

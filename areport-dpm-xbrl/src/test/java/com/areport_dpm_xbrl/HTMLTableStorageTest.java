package com.areport_dpm_xbrl;

import javafx.scene.control.TableCell;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.spy;

public class HTMLTableStorageTest {
    private HTMLTableStorage sut;

    @BeforeEach
    public void setUp() {
        sut = spy(new HTMLTableStorage(23, true));
    }

    @Test
    public void t1() {
        sut.setUseTGroups(true);
        assertTrue(sut.getUseTGroups());
    }

    @Test
    public void t2() {
        sut.setAutoFill("Hi");
        assertEquals("Hi", sut.getAutoFill());
    }

    @Test
    public void t3() {
        sut = spy(new HTMLTableStorage());

        sut.setAutoGrow(true);

        assertTrue(sut.getAutoGrow());
    }

    @Test
    public void t4() {
        sut.setColCount(2);
        sut.setRowCount(5);


        assertEquals(5, sut.getRowCount());
        assertEquals(2, sut.getColCount());
        assertEquals(2, sut.getColCount(null));
    }

    @Test
    public void t5() {
        Map<Integer, Map<Integer, HTMLTableStorage.Cell>> structure = new HashMap<>();
        HTMLTableStorage.Cell cell = spy(new HTMLTableStorage.Cell());
        Map<Integer, HTMLTableStorage.Cell> map = new HashMap<>();

        Integer i = 5;
        map.put(i, cell);
        structure.put(5, map);
        ReflectionTestUtils.setField(sut, "structure", structure);

        assertEquals(1, sut.getColCount(i));
    }

    @Test
    public void t6() {
        Map<String, String> attributes = new HashMap<>();
        attributes.put("abc", "abc");
        HTMLTableStorage.Cell cell = spy(new HTMLTableStorage.Cell(attributes));

        assertEquals("abc", cell.getAttributes().get("abc"));
        cell.updateAttributes(attributes);
        cell.setAttributes(attributes);
        Integer i = 5;
        cell.setContents(i);
        cell.setType("Hello");
        assertEquals("abc", cell.getAttributes().get("abc"));
        assertEquals(5, cell.getContents());
        assertEquals("Hello", cell.getType());
    }

    @Test
    public void t7() {
        Map<Integer, Map<Integer, HTMLTableStorage.Cell>> structure = new HashMap<>();
        HTMLTableStorage.Cell cell = spy(new HTMLTableStorage.Cell());
        Map<Integer, HTMLTableStorage.Cell> map = new HashMap<>();

        Integer i = 0;
        map.put(i, cell);
        structure.put(5, map);
        ReflectionTestUtils.setField(sut, "structure", structure);
        ReflectionTestUtils.setField(sut, "cols", 1);

        sut.setRowType(5, "str");
        assertEquals("str", structure.get(5).get(0).getType());
    }

    @Test
    public void t8() {
        Map<Integer, Map<Integer, HTMLTableStorage.Cell>> structure = new HashMap<>();
        HTMLTableStorage.Cell cell = spy(new HTMLTableStorage.Cell());
        Map<Integer, HTMLTableStorage.Cell> map = new HashMap<>();

        Integer i = 0;
        map.put(i, cell);
        structure.put(0, map);
        ReflectionTestUtils.setField(sut, "structure", structure);
        ReflectionTestUtils.setField(sut, "rows", 1);

        sut.setColType(0, "str");
        assertEquals("str", structure.get(0).get(0).getType());
    }

    @Test
    public void t9() {
        Map<Integer, Map<Integer, HTMLTableStorage.Cell>> structure = new HashMap<>();
        HTMLTableStorage.Cell cell = spy(new HTMLTableStorage.Cell());
        Map<Integer, HTMLTableStorage.Cell> map = new HashMap<>();
        Map<String, String> attributes = new HashMap<>();
        attributes.put("abc", "abc");

        Integer i = 0;
        map.put(i, cell);
        structure.put(0, map);
        ReflectionTestUtils.setField(sut, "structure", structure);
        ReflectionTestUtils.setField(sut, "rows", 1);

        sut.setCellAttributes(0, 0, attributes);
        assertEquals("abc", structure.get(0).get(0).getAttributes().get("abc"));
    }

    @Test
    public void t10() {
        Map<Integer, Map<Integer, HTMLTableStorage.Cell>> structure = new HashMap<>();
        HTMLTableStorage.Cell cell = spy(new HTMLTableStorage.Cell());
        Map<Integer, HTMLTableStorage.Cell> map = new HashMap<>();
        Map<String, String> attributes = new HashMap<>();
        attributes.put("abc", "abc");

        Integer i = 0;
        map.put(i, cell);
        structure.put(0, map);
        ReflectionTestUtils.setField(sut, "structure", structure);
        ReflectionTestUtils.setField(sut, "rows", 1);
        structure.get(0).get(0).setAttributes(attributes);

        sut.getCellAttributes(0, 0);
        assertEquals("abc", structure.get(0).get(0).getAttributes().get("abc"));
    }

    @Test
    public void t11() {
        Map<Integer, Map<Integer, HTMLTableStorage.Cell>> structure = new HashMap<>();
        HTMLTableStorage.Cell cell = spy(new HTMLTableStorage.Cell());
        Map<Integer, HTMLTableStorage.Cell> map = new HashMap<>();
        Map<String, String> attributes = new HashMap<>();
        attributes.put("abc", "abc");

        Integer i = 0;
        map.put(i, cell);
        structure.put(0, map);
        ReflectionTestUtils.setField(sut, "structure", structure);
        ReflectionTestUtils.setField(sut, "rows", 1);
        structure.get(0).get(0).setAttributes(attributes);

        sut.getCellAttributes(0, 0);
        assertEquals("abc", structure.get(0).get(0).getAttributes().get("abc"));
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                sut.getCellAttributes(1, 0));
    }

    @Test
    public void t12() {
        Map<Integer, Map<Integer, HTMLTableStorage.Cell>> structure = new HashMap<>();
        HTMLTableStorage.Cell cell = spy(new HTMLTableStorage.Cell());
        Map<Integer, HTMLTableStorage.Cell> map = new HashMap<>();
        Map<String, String> attributes = new HashMap<>();
        attributes.put("abc", "abc");

        Integer i = 0;
        map.put(i, cell);
        structure.put(0, map);
        ReflectionTestUtils.setField(sut, "structure", structure);
        ReflectionTestUtils.setField(sut, "rows", 1);

        sut.updateCellAttributes(0, 0, attributes);
        assertEquals("abc", structure.get(0).get(0).getAttributes().get("abc"));
    }

    @Test
    public void t13() {
        Map<Integer, Map<Integer, HTMLTableStorage.Cell>> structure = new HashMap<>();
        HTMLTableStorage.Cell cell = spy(new HTMLTableStorage.Cell());
        Map<Integer, HTMLTableStorage.Cell> map = new HashMap<>();
        Map<String, String> attributes = new HashMap<>();
        attributes.put("abc", "abc");

        Integer i = 0;
        map.put(i, cell);
        structure.put(0, map);
        structure.get(0).get(0).setContents("__SPANNED__");
        ReflectionTestUtils.setField(sut, "structure", structure);
        ReflectionTestUtils.setField(sut, "rows", 1);
        structure.get(0).get(0).setAttributes(attributes);

        Object[] obj = new Object[1];
        obj[0] = "Str";
        sut.setCellContents(0, 0, obj, "str");
//        assertEquals("abc", structure.get(0).get(0).getAttributes().get("abc"));
//        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
//                sut.getCellAttributes(1, 0));
    }

    @Test
    public void t14() {
        Map<Integer, Map<Integer, HTMLTableStorage.Cell>> structure = new HashMap<>();
        HTMLTableStorage.Cell cell = spy(new HTMLTableStorage.Cell());
        Map<Integer, HTMLTableStorage.Cell> map = new HashMap<>();
        Map<String, String> attributes = new HashMap<>();
        attributes.put("abc", "abc");

        Integer i = 0;
        map.put(i, cell);
        structure.put(0, map);
//        structure.get(0).get(0).setContents("__SPANNED__");
        ReflectionTestUtils.setField(sut, "structure", structure);
        ReflectionTestUtils.setField(sut, "rows", 1);
        structure.get(0).get(0).setAttributes(attributes);

//        Object[] obj = new Object[1];
//        obj[0] = "Str";
        sut.setCellContents(0, 0, "STR", "str");
        assertEquals("STR", structure.get(0).get(0).getContents());
        assertEquals("str", structure.get(0).get(0).getType());
//        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
//                sut.getCellAttributes(1, 0));
    }

    @Test
    public void t15() {
        Map<Integer, Map<Integer, HTMLTableStorage.Cell>> structure = new HashMap<>();
        HTMLTableStorage.Cell cell = spy(new HTMLTableStorage.Cell());
        Map<Integer, HTMLTableStorage.Cell> map = new HashMap<>();
        Map<String, String> attributes = new HashMap<>();
        attributes.put("abc", "abc");

        Integer i = 0;
        map.put(i, cell);
        structure.put(0, map);
        structure.get(0).get(0).setContents("__SPANNED__");
        ReflectionTestUtils.setField(sut, "structure", structure);
        ReflectionTestUtils.setField(sut, "rows", 1);
        structure.get(0).get(0).setAttributes(attributes);

//        Object[] obj = new Object[1];
//        obj[0] = "Str";

        assertNull(sut.getCellContents(0, 0));
    }

    @Test
    public void t16() {
        Map<Integer, Map<Integer, HTMLTableStorage.Cell>> structure = new HashMap<>();
        HTMLTableStorage.Cell cell = spy(new HTMLTableStorage.Cell());
        Map<Integer, HTMLTableStorage.Cell> map = new HashMap<>();
        Map<String, String> attributes = new HashMap<>();
        attributes.put("abc", "abc");

        Integer i = 0;
        map.put(i, cell);
        structure.put(0, map);
//        structure.get(0).get(0).setContents("__SPANNED__");
        ReflectionTestUtils.setField(sut, "structure", structure);
        ReflectionTestUtils.setField(sut, "rows", 1);
        structure.get(0).get(0).setAttributes(attributes);

//        Object[] obj = new Object[1];
//        obj[0] = "Str";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                sut.getCellContents(1, 1));
    }

    @Test
    public void t17() {
        Map<Integer, Map<Integer, HTMLTableStorage.Cell>> structure = new HashMap<>();
        HTMLTableStorage.Cell cell = spy(new HTMLTableStorage.Cell());
        Map<Integer, HTMLTableStorage.Cell> map = new HashMap<>();
        Map<String, String> attributes = new HashMap<>();
        attributes.put("abc", "abc");

        Integer i = 0;
        map.put(i, cell);
        structure.put(0, map);
//        structure.get(0).get(0).setContents("__SPANNED__");
        ReflectionTestUtils.setField(sut, "structure", structure);
        ReflectionTestUtils.setField(sut, "rows", 1);
        structure.get(0).get(0).setContents("str");

//        Object[] obj = new Object[1];
//        obj[0] = "Str";

        assertEquals("str", sut.getCellContents(0, 0));
    }


    @Test
    public void t18() {
        Map<Integer, Map<Integer, HTMLTableStorage.Cell>> structure = new HashMap<>();
        HTMLTableStorage.Cell cell = spy(new HTMLTableStorage.Cell());
        Map<Integer, HTMLTableStorage.Cell> map = new HashMap<>();
        Map<String, String> attributes = new HashMap<>();
        attributes.put("abc", "abc");

        Integer i = 0;
        map.put(i, cell);
        structure.put(0, map);
//        structure.get(0).get(0).setContents("__SPANNED__");
        ReflectionTestUtils.setField(sut, "structure", structure);
        ReflectionTestUtils.setField(sut, "rows", 1);
        structure.get(0).get(0).setAttributes(attributes);
        sut.setHeaderContents(0, 0, "Str", attributes);

        assertEquals("TH", structure.get(0).get(0).getType());
    }

    @Test
    public void t19() {
        Map<Integer, Map<Integer, HTMLTableStorage.Cell>> structure = new HashMap<>();
        HTMLTableStorage.Cell cell = spy(new HTMLTableStorage.Cell());
        Map<Integer, HTMLTableStorage.Cell> map = new HashMap<>();
        Map<String, String> attributes = new HashMap<>();
        attributes.put("abc", "abc");

        Integer i = 0;
        map.put(i, cell);
        structure.put(0, map);
        structure.get(0).get(0).setContents("__SPANNED__");
        ReflectionTestUtils.setField(sut, "structure", structure);
        ReflectionTestUtils.setField(sut, "rows", 1);
        structure.get(0).get(0).setAttributes(attributes);

        Object[] obj = new Object[1];
        obj[0] = "Str";

        assertEquals(1, sut.addRow(null, attributes, "str", false));
    }

    @Test
    public void t20() {
        Map<Integer, Map<Integer, HTMLTableStorage.Cell>> structure = new HashMap<>();
        HTMLTableStorage.Cell cell = spy(new HTMLTableStorage.Cell());
        Map<Integer, HTMLTableStorage.Cell> map = new HashMap<>();
        Map<String, String> attributes = new HashMap<>();
        attributes.put("abc", "abc");

        Integer i = 0;
        map.put(i, cell);
        structure.put(0, map);
//        structure.get(0).get(0).setContents("__SPANNED__");
        ReflectionTestUtils.setField(sut, "structure", structure);
        ReflectionTestUtils.setField(sut, "rows", 0);
        structure.get(0).get(0).setAttributes(attributes);

        Object[] obj = new Object[1];
        obj[0] = "Str";

        assertEquals(0, sut.addRow(obj, attributes, "td", false));
    }

    @Test
    public void t21() {
        Map<Integer, Map<Integer, HTMLTableStorage.Cell>> structure = new HashMap<>();
        HTMLTableStorage.Cell cell = spy(new HTMLTableStorage.Cell());
        Map<Integer, HTMLTableStorage.Cell> map = new HashMap<>();
        Map<String, String> attributes = new HashMap<>();
        attributes.put("abc", "abc");

        Integer i = 0;
        map.put(i, cell);
        structure.put(0, map);
//        structure.get(0).get(0).setContents("__SPANNED__");
        ReflectionTestUtils.setField(sut, "structure", structure);
        ReflectionTestUtils.setField(sut, "rows", 0);
        ReflectionTestUtils.setField(sut, "cols", 1);
        structure.get(0).get(0).setAttributes(attributes);

        Object[] obj = new Object[1];
        obj[0] = "Str";

        assertEquals(0, sut.addRow(obj, attributes, "th", false));
    }

    @Test
    public void t22() {
        Map<Integer, Map<Integer, HTMLTableStorage.Cell>> structure = new HashMap<>();
        HTMLTableStorage.Cell cell = spy(new HTMLTableStorage.Cell());
        Map<Integer, HTMLTableStorage.Cell> map = new HashMap<>();
        Map<String, String> attributes = new HashMap<>();
        attributes.put("abc", "abc");

        Integer i = 0;
        map.put(i, cell);
        structure.put(0, map);
//        structure.get(0).get(0).setContents("__SPANNED__");
        ReflectionTestUtils.setField(sut, "structure", structure);
        ReflectionTestUtils.setField(sut, "rows", 0);
        ReflectionTestUtils.setField(sut, "cols", 1);
        structure.get(0).get(0).setAttributes(attributes);

        Object[] obj = new Object[1];
        obj[0] = "Str";

        sut.updateRowAttributes(0, attributes, false);
        assertEquals("abc", structure.get(0).get(0).getAttributes().get("abc"));
    }

    @Test
    public void t23() {
        Map<Integer, Map<Integer, HTMLTableStorage.Cell>> structure = new HashMap<>();
        HTMLTableStorage.Cell cell = spy(new HTMLTableStorage.Cell());
        Map<Integer, HTMLTableStorage.Cell> map = new HashMap<>();
        Map<String, String> attributes = new HashMap<>();
        attributes.put("abc", "abc");

        Integer i = 0;
        map.put(i, cell);
        structure.put(0, map);
//        structure.get(0).get(0).setContents("__SPANNED__");
        ReflectionTestUtils.setField(sut, "structure", structure);
        ReflectionTestUtils.setField(sut, "rows", 0);
        ReflectionTestUtils.setField(sut, "cols", 1);
        structure.get(0).get(0).setAttributes(attributes);

        Object[] obj = new Object[1];
        obj[0] = "Str";

        sut.updateRowAttributes(0, attributes, true);
        assertTrue(structure.get(0).containsKey(-1));
    }

    @Test
    public void t24() {
        assertEquals("", sut.toHtml());
    }

}

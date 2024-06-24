package com.areport_dpm_xbrl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;

import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.spy;

public class DomToArrayTest {
    private DomToArray sut;

    @BeforeEach
    public void setUp(){
        sut = spy(new DomToArray());
    }

    @Test
    public void t1(){
        String xmlContent = "<root><child>test</child></root>";
        String path = "test.xml";

        try {
            Files.write(Paths.get(path), xmlContent.getBytes());
            Document doc = DomToArray.invoke(path);
            assertNotNull(doc);
            assertEquals("root", doc.getDocumentElement().getNodeName());
        } catch (Exception e) {
            fail("Exception should not have been thrown: " + e.getMessage());
        } finally {
            try {
                Files.deleteIfExists(Paths.get(path));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    @Test
    public void t8(){
        String xmlContent = "<root><child>test</child></root>";
        String path = "test.xml";

        try {
            Files.write(Paths.get(path), xmlContent.getBytes());
            Map<String, Object> expected = DomToArray.getArray(path);
            assertNotNull(expected);
            assertEquals(null, expected.get("root"));
        } catch (Exception e) {
            fail("Exception should not have been thrown: " + e.getMessage());
        } finally {
            try {
                Files.deleteIfExists(Paths.get(path));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Test
    public void t2(){
        String xmlContent = "<root><child>test</child>";
        String path = "invalid_test.xml";

        try {
            Files.write(Paths.get(path), xmlContent.getBytes());
            Document doc = DomToArray.invoke(path);
            assertNull(doc);
        } catch (Exception e) {
            fail("Exception should not have been thrown: " + e.getMessage());
        } finally {
            try {
                Files.deleteIfExists(Paths.get(path));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testInvokeNonExistentFile() {
        String path = "non_existent_file.xml";
        Document doc = DomToArray.invoke(path);
        assertNull(doc);
    }

    @Test
    public void t4(){
        Map<String, Object> map = new HashMap<>();
        String str1 = "efg";
        map.put("abc", str1);
        List<Map<String, Object>> arr= new ArrayList<>();
        arr.add(map);
        String field = "abc";
        String value = "efg";

        sut.searchMultiDim(arr, field, value);
    }

    @Test
    public void t5(){
        Map<String, Object> map = new HashMap<>();
        String str1 = "http://www.eba.europa.eu/xbrl/role/dpm-db-id";
        map.put("role", str1);
        List<Map<String, Object>> arr= new ArrayList<>();
        arr.add(map);
        String field = "role";
        String value = "http://www.eba.europa.eu/xbrl/role/dpm-db-id";

        sut.searchMultiDimMultiVal(arr, field, value);
    }


    @Test
    public void t6(){
        Map<String, Object> map = new HashMap<>();
        String str1 = "http://www.eba.eur.eu/xbrl/role/dpm-db-id";
        map.put("role", str1);
        List<Map<String, Object>> arr= new ArrayList<>();
        arr.add(map);
        String field = "role";
        String value = "http://www.eba.europa.eu/xbrl/role/dpm-db-id";

        sut.searchMultiDimMultiVal(arr, field, value);
    }

    @Test
    public void t7() throws IOException {
        Path tempDir = Files.createTempDirectory("testDir");
        Path file1 = Files.createFile(tempDir.resolve("file1.xsd"));
        Path file2 = Files.createFile(tempDir.resolve("file2.txt"));

        List<String> stringList = new ArrayList<>();
        stringList.add("file1");

        List<String> result = DomToArray.getPath(tempDir.toString(), stringList, null);

        assertEquals(1, result.size());
        assertTrue(result.get(0).endsWith("file1.xsd"));

        Files.walk(tempDir)
                .map(Path::toFile)
                .forEach(File::delete);
    }

    @Test
    public void testGetPathWithMatchingFiles() throws Exception {
        Path tempDir = Files.createTempDirectory("testDir");
        Path file1 = Files.createFile(tempDir.resolve("file1.xsd"));
        Path file2 = Files.createFile(tempDir.resolve("file2.txt"));

        List<String> stringList = new ArrayList<>();
        stringList.add("file1");

        List<String> result = DomToArray.getPath(tempDir.toString(), stringList, null);

        assertEquals(1, result.size());
        assertTrue(result.get(0).endsWith("file1.xsd"));
    }


    @Test
    public void testGetPathWithNoMatchingFiles() throws Exception {
        Path tempDir = Files.createTempDirectory("testDir");
        Path file1 = Files.createFile(tempDir.resolve("file1.txt"));
        Path file2 = Files.createFile(tempDir.resolve("file2.txt"));

        List<String> stringList = new ArrayList<>();
        stringList.add("file1");

        List<String> result = DomToArray.getPath(tempDir.toString(), stringList, null);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetPathWithNonExistentDirectory() {
        List<String> stringList = new ArrayList<>();
        stringList.add("file1");

        List<String> result = DomToArray.getPath("nonExistentDir", stringList, null);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetPathWithReturnValue() throws Exception {
        Path tempDir = Files.createTempDirectory("testDir");
        Path file1 = Files.createFile(tempDir.resolve("file1.xsd"));

        List<String> stringList = new ArrayList<>();
        stringList.add("file1");

        List<String> result = DomToArray.getPath(tempDir.toString(), stringList, "returnValue");

        assertEquals(1, result.size());
        assertTrue(result.get(0).endsWith("file1.xsd"));
    }

    @Test
    public void testBuildUrlWithMissingParts() {
        Map<String, String> parts = new HashMap<>();
        parts.put("scheme", "https");

        // Assuming Config.owner is "example.com"
        String expected = "https://www.eba.europa.eu";
        String result = DomToArray.buildUrl(parts);

        assertEquals(expected, result);
    }

    @Test
    public void testNeedleFound() {
        String haystack = "This is a simple test string.";
        String[] needles = {"simple", "test"};

        int result = DomToArray.strposArr(haystack, needles);

        assertEquals(10, result);
    }

    @Test
    public void testNeedleNotFound() {
        String haystack = "This is a simple test string.";
        String[] needles = {"none", "missing"};

        int result = DomToArray.strposArr(haystack, needles);

        assertEquals(-1, result);
    }

    @Test
    public void testEmptyHaystack() {
        String haystack = "";
        String[] needles = {"simple", "test"};

        int result = DomToArray.strposArr(haystack, needles);

        assertEquals(-1, result);
    }
    @Test
    public void testEmptyNeedles() {
        String haystack = "This is a simple test string.";
        String[] needles = {};

        int result = DomToArray.strposArr(haystack, needles);

        assertEquals(-1, result);
    }


    @Test
    public void testSingleLevelList() {
        List<Object> array = Arrays.asList(1, 2, 3);
        List<Object> expected = Arrays.asList(1, 2, 3);
        List<Object> result = DomToArray.multidimensionalArrToSingle(array);

        assertEquals(expected, result);
    }

    @Test
    public void testNestedList() {
        List<Object> array = Arrays.asList(1, Arrays.asList(2, 3), 4);
        List<Object> expected = Arrays.asList(1, 2, 3, 4);
        List<Object> result = DomToArray.multidimensionalArrToSingle(array);

        assertEquals(expected, result);
    }

    @Test
    public void testDeeplyNestedList() {
        List<Object> array = Arrays.asList(1, Arrays.asList(2, Arrays.asList(3, Arrays.asList(4, 5))));
        List<Object> expected = Arrays.asList(1, 2, 3, 4, 5);
        List<Object> result = DomToArray.multidimensionalArrToSingle(array);

        assertEquals(expected, result);
    }

}











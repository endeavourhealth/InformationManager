package org.endeavourhealth.informationmodel.api.framework.helper;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class CsvHelperTest {
    @Test
    public void parseLineDefaults() throws Exception {
        String csvLine = "\"Field1\",\"Field2\",\"Field3\"";
        List<String> result = CsvHelper.parseLine(csvLine);
        assertArrayEquals(result.toArray(), new String[]{"Field1", "Field2", "Field3"});
    }

    @Test
    public void parseLineDefaultQuote() throws Exception {
        String csvLine = "\"Field1\"|\"Field2\"|\"Field3\"";
        List<String> result = CsvHelper.parseLine(csvLine,'|');
        assertArrayEquals(result.toArray(), new String[]{"Field1", "Field2", "Field3"});
    }

    @Test
    public void parseLine() throws Exception {
        String csvLine = "'Field1'|'Field2'|'Field3'";
        List<String> result = CsvHelper.parseLine(csvLine,'|','\'');
        assertArrayEquals(result.toArray(), new String[]{"Field1", "Field2", "Field3"});
    }

    @Test
    public void parseLineSpaceDefaults() throws Exception {
        String csvLine = "\"Field1\",\"Field2\",\"Field3\"";
        List<String> result = CsvHelper.parseLine(csvLine,' ',' ');
        assertArrayEquals(result.toArray(), new String[]{"Field1", "Field2", "Field3"});
    }
    @Test
    public void parseLineNull() throws Exception {
        List<String> result = CsvHelper.parseLine(null);
        assertNotNull(result);
        assertEquals(result.size(), 0);
    }
    @Test

    public void parseLineEmpty() throws Exception {
        List<String> result = CsvHelper.parseLine("");
        assertNotNull(result);
        assertEquals(result.size(), 0);
    }
}
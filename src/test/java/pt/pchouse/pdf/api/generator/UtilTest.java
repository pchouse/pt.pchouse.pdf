package pt.pchouse.pdf.api.generator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UtilTest
{

    /**
     * Tests for the Util class's parseBool method, which is responsible for
     * parsing a String input into a boolean value. The method should return
     * true or false based on the input while throwing an exception for invalid inputs.
     */

    @Test
    void testParseBoolWithTrueInput() throws Exception {
        String  input  = "true";
        boolean result = Util.parseBool(input);
        assertTrue(result);
    }

    @Test
    void testParseBoolWithFalseInput() throws Exception {
        String  input  = "false";
        boolean result = Util.parseBool(input);
        assertFalse(result);
    }

    @Test
    void testParseBoolWithYesInput() throws Exception {
        String  input  = "yes";
        boolean result = Util.parseBool(input);
        assertTrue(result);
    }

    @Test
    void testParseBoolWithNoInput() throws Exception {
        String  input  = "no";
        boolean result = Util.parseBool(input);
        assertFalse(result);
    }

    @Test
    void testParseBoolWithOnInput() throws Exception {
        String  input  = "on";
        boolean result = Util.parseBool(input);
        assertTrue(result);
    }

    @Test
    void testParseBoolWithOffInput() throws Exception {
        String  input  = "off";
        boolean result = Util.parseBool(input);
        assertFalse(result);
    }

    @Test
    void testParseBoolWith1Input() throws Exception {
        String  input  = "1";
        boolean result = Util.parseBool(input);
        assertTrue(result);
    }

    @Test
    void testParseBoolWith0Input() throws Exception {
        String  input  = "0";
        boolean result = Util.parseBool(input);
        assertFalse(result);
    }

    @Test
    void testParseBoolWithMixedCaseTrueInput() throws Exception {
        String  input  = "TrUe";
        boolean result = Util.parseBool(input);
        assertTrue(result);
    }

    @Test
    void testParseBoolThrowsExceptionForNullInput() {
        String    input     = null;
        Exception exception = assertThrows(Exception.class, () -> Util.parseBool(input));
        assertEquals("A null value is not parseble to boolean", exception.getMessage());
    }

    @Test
    void testParseBoolThrowsExceptionForInvalidInput() {
        String    input     = "invalid";
        Exception exception = assertThrows(Exception.class, () -> Util.parseBool(input));
        assertEquals("Value '" + input + "' is not parseble to boolean", exception.getMessage());
    }
}
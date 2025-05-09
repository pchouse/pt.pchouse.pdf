package pt.pchouse.pdf.api.request.datasource;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class XmlHttpsTest
{

    /**
     * Test the {@link XmlHttps#equals(Object)} method.
     */

    @Test
    void testEqualsWithSameInstance() {
        XmlHttps xmlHttps = new XmlHttps();
        assertEquals(xmlHttps, xmlHttps, "An object should be equal to itself.");
    }

    @Test
    void testEqualsWithNull() {
        XmlHttps xmlHttps = new XmlHttps();
        assertNotEquals(null, xmlHttps, "An object should not be equal to null.");
    }

    @Test
    void testEqualsWithDifferentType() {
        XmlHttps xmlHttps            = new XmlHttps();
        Object   differentTypeObject = new Object();
        assertNotEquals(xmlHttps, differentTypeObject, "An object should not be equal to an object of a different type.");
    }

    @Test
    void testEqualsWithEqualProperties() {
        XmlHttps xmlHttps1 = new XmlHttps();
        XmlHttps xmlHttps2 = new XmlHttps();
        assertEquals(xmlHttps1, xmlHttps2, "Two objects with the same properties should be considered equal.");
    }

    @Test
    void testEqualsWithDifferentProperties() {
        XmlHttps xmlHttps1 = new XmlHttps();
        xmlHttps1.setUrl("https://www.example.com");
        XmlHttps xmlHttps2 = new XmlHttps();
        xmlHttps2.setUrl("https://www.example.com/test");

        assertNotEquals(xmlHttps1, xmlHttps2, "Two objects with different properties should not be considered equal.");
    }
}
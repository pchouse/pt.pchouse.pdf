package pt.pchouse.pdf.api.request.datasource;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class XmlHttpTest
{

    @Test
    void testEquals_SameInstance() {
        XmlHttp xmlHttp1 = new XmlHttp();

        assertEquals(xmlHttp1, xmlHttp1, "The equals method should return true when comparing the same instance.");
    }

    @Test
    void testEquals_NullObject() {
        XmlHttp xmlHttp1 = new XmlHttp();

        assertNotEquals(null, xmlHttp1, "The equals method should return false when comparing with a null object.");
    }

    @Test
    void testEquals_DifferentClass() {
        XmlHttp xmlHttp1    = new XmlHttp();
        Object  otherObject = new Object();

        assertNotEquals(xmlHttp1, otherObject, "The equals method should return false when comparing with an object of a different class.");
    }

    @Test
    void testEquals_DifferentInstanceSameValues() {
        XmlHttp xmlHttp1 = new XmlHttp();
        XmlHttp xmlHttp2 = new XmlHttp();

        assertEquals(xmlHttp1, xmlHttp2, "The equals method should return true when comparing two different instances with the same values.");
    }

    @Test
    void testEquals_DifferentInstanceDifferentValues() {
        XmlHttp xmlHttp1 = new XmlHttp();
        xmlHttp1.setUrl("http://www.example.com");

        XmlHttp xmlHttp2 = new XmlHttp();
        xmlHttp2.setUrl("http://www.example.com/different");

        assertNotEquals(xmlHttp1, xmlHttp2, "The equals method should return false when comparing two instances with different values.");
    }
}
package pt.pchouse.pdf.api.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class PdfPropertiesTest
{

    @Test
    void testEquals_SameObject() {
        PdfProperties pdfProperties = new PdfProperties();
        pdfProperties.setUserPassword("userPass123");
        pdfProperties.setOwnerPassword("ownerPass123");
        pdfProperties.setJavascript("allow");

        assertEquals(pdfProperties, pdfProperties);
    }

    @Test
    void testEquals_EqualObjects() {
        PdfProperties pdfProperties1 = new PdfProperties();
        pdfProperties1.setUserPassword("userPass123");
        pdfProperties1.setOwnerPassword("ownerPass123");
        pdfProperties1.setJavascript("allow");

        PdfProperties pdfProperties2 = new PdfProperties();
        pdfProperties2.setUserPassword("userPass123");
        pdfProperties2.setOwnerPassword("ownerPass123");
        pdfProperties2.setJavascript("allow");

        assertEquals(pdfProperties1, pdfProperties2);
    }

    @Test
    void testEquals_DifferentUserPassword() {
        PdfProperties pdfProperties1 = new PdfProperties();
        pdfProperties1.setUserPassword("userPass123");
        pdfProperties1.setOwnerPassword("ownerPass123");
        pdfProperties1.setJavascript("allow");

        PdfProperties pdfProperties2 = new PdfProperties();
        pdfProperties2.setUserPassword("userPass456");
        pdfProperties2.setOwnerPassword("ownerPass123");
        pdfProperties2.setJavascript("allow");

        assertNotEquals(pdfProperties1, pdfProperties2);
    }

    @Test
    void testEquals_DifferentOwnerPassword() {
        PdfProperties pdfProperties1 = new PdfProperties();
        pdfProperties1.setUserPassword("userPass123");
        pdfProperties1.setOwnerPassword("ownerPass123");
        pdfProperties1.setJavascript("allow");

        PdfProperties pdfProperties2 = new PdfProperties();
        pdfProperties2.setUserPassword("userPass123");
        pdfProperties2.setOwnerPassword("ownerPass456");
        pdfProperties2.setJavascript("allow");

        assertNotEquals(pdfProperties1, pdfProperties2);
    }

    @Test
    void testEquals_DifferentJavascript() {
        PdfProperties pdfProperties1 = new PdfProperties();
        pdfProperties1.setUserPassword("userPass123");
        pdfProperties1.setOwnerPassword("ownerPass123");
        pdfProperties1.setJavascript("allow");

        PdfProperties pdfProperties2 = new PdfProperties();
        pdfProperties2.setUserPassword("userPass123");
        pdfProperties2.setOwnerPassword("ownerPass123");
        pdfProperties2.setJavascript("deny");

        assertNotEquals(pdfProperties1, pdfProperties2);
    }

    @Test
    void testEquals_NullObject() {
        PdfProperties pdfProperties = new PdfProperties();
        pdfProperties.setUserPassword("userPass123");
        pdfProperties.setOwnerPassword("ownerPass123");
        pdfProperties.setJavascript("allow");

        assertNotEquals(null, pdfProperties);
    }

    @Test
    void testEquals_DifferentClass() {
        PdfProperties pdfProperties = new PdfProperties();
        pdfProperties.setUserPassword("userPass123");
        pdfProperties.setOwnerPassword("ownerPass123");
        pdfProperties.setJavascript("allow");

        Object otherObject = new Object();

        assertNotEquals(pdfProperties, otherObject);
    }
}
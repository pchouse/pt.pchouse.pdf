package pt.pchouse.pdf.api.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pt.pchouse.pdf.api.sign.ISignPDF;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;

@SpringBootTest
class RequestSignTest
{

    /**
     * Tests the getPdfBase64 method when no value is set.
     * The method should return null in this case.
     */
    @Test
    void testGetPdfBase64InitiallyNull() {
        RequestSign<ISignPDF> requestSign = new RequestSign<>();
        assertNull(requestSign.getPdfBase64(), "The pdfBase64 should be null initially.");
    }

    /**
     * Tests the getPdfBase64 method when a valid value is set.
     * The method should return the set value.
     */
    @Test
    void testGetPdfBase64WithSetValue() {
        String                expectedPdfBase64 = "sampleBase64String";
        RequestSign<ISignPDF> requestSign       = new RequestSign<>();
        requestSign.setPdfBase64(expectedPdfBase64);
        assertEquals(expectedPdfBase64, requestSign.getPdfBase64(), "The pdfBase64 should match the set value.");
    }

    /**
     * Tests the getPdfBase64 method when value is updated multiple times.
     * The method should return the latest updated value.
     */
    @Test
    void testGetPdfBase64WithUpdatedValue() {
        String                firstValue  = "firstBase64";
        String                secondValue = "secondBase64";
        RequestSign<ISignPDF> requestSign = new RequestSign<>();
        requestSign.setPdfBase64(firstValue);
        requestSign.setPdfBase64(secondValue);
        assertEquals(secondValue, requestSign.getPdfBase64(), "The pdfBase64 should match the latest set value.");
    }

    /**
     * Tests the getPdfBase64 method when value is updated multiple times.
     * The method should return the latest updated value.
     */
    @Test
    void testGetISignPdfWithUpdatedValue() {
        ISignPDF                firstValue  = mock(ISignPDF.class);
        ISignPDF                secondValue = mock(ISignPDF.class);
        RequestSign<ISignPDF> requestSign = new RequestSign<>();
        requestSign.setSignPDF(firstValue);
        assertEquals(firstValue, requestSign.getSignPDF(), "The ISignPdf should match the latest set value.");
        requestSign.setSignPDF(secondValue);
        assertEquals(secondValue, requestSign.getSignPDF(), "The ISignPdf should match the latest set value.");
    }

}
package pt.pchouse.pdf.api.request;

import org.junit.jupiter.api.Test;
import pt.pchouse.pdf.api.print.AfterPrintOperations;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class RequestPrintTest
{

    /**
     * Tests for the `getPdfBase64` method in the `RequestPrint` class.
     * <p>
     * The `RequestPrint` class is a scoped Spring Component used to define
     * details for a print request. It includes properties such as `pdfBase64`
     * (representing the PDF data in base64 format), `printerName`, and
     * operations to perform after printing.
     * <p>
     * The `getPdfBase64` method retrieves the `pdfBase64` field,
     * which can be set with valid base64-encoded data.
     */

    @Test
    void testGetPdfBase64_WhenPdfBase64IsNull_ShouldReturnNull() {
        // Arrange
        RequestPrint requestPrint = new RequestPrint();

        // Act
        String result = requestPrint.getPdfBase64();

        // Assert
        assertNull(result, "getPdfBase64 should return null when pdfBase64 is not set");
    }

    @Test
    void testGetPdfBase64_WhenPdfBase64IsSet_ShouldReturnSameValue() {
        // Arrange
        RequestPrint requestPrint   = new RequestPrint();
        String       pdfBase64Value = "dGVzdERhdGE="; // Example base64 string

        // Act
        requestPrint.setPdfBase64(pdfBase64Value);
        String result = requestPrint.getPdfBase64();

        // Assert
        assertEquals(pdfBase64Value, result, "getPdfBase64 should return the same value that was set");
    }

    @Test
    void testGetPdfBase64_WhenPdfBase64IsUpdated_ShouldReturnUpdatedValue() {
        // Arrange
        RequestPrint requestPrint          = new RequestPrint();
        String       initialPdfBase64Value = "dGVzdEluaXRpYWw="; // Initial base64 string
        String       updatedPdfBase64Value = "dGVzdFVwZGF0ZWQ="; // Updated base64 string

        // Act
        requestPrint.setPdfBase64(initialPdfBase64Value);
        requestPrint.setPdfBase64(updatedPdfBase64Value);
        String result = requestPrint.getPdfBase64();

        // Assert
        assertEquals(updatedPdfBase64Value, result, "getPdfBase64 should return the latest set value");
    }


    @Test
    void testGetAfterPrintOperations_WhenNotSet_ShouldReturnEmptyList() {
        // Arrange
        RequestPrint requestPrint = new RequestPrint();

        // Act
        var result = requestPrint.getAfterPrintOperations();

        // Assert
        assertNotNull(result, "getAfterPrintOperations should not return null even if not set");
        assertTrue(result.isEmpty(), "getAfterPrintOperations should return an empty list when no data is set");
    }

    @Test
    void testGetAfterPrintOperations_WhenSet_ShouldReturnSameValue() {
        // Arrange
        RequestPrint                    requestPrint = new RequestPrint();
        ArrayList<AfterPrintOperations> operations   = new ArrayList<>();
        operations.add(AfterPrintOperations.AFTER_PRINT_CUT_PAPER);

        // Act
        requestPrint.setAfterPrintOperations(operations);
        ArrayList<AfterPrintOperations> result = requestPrint.getAfterPrintOperations();

        // Assert
        assertEquals(operations, result, "getAfterPrintOperations should return the same list that was set");
    }

    @Test
    void testGetAfterPrintOperations_WhenUpdated_ShouldReturnUpdatedValue() {
        // Arrange
        RequestPrint                    requestPrint      = new RequestPrint();
        ArrayList<AfterPrintOperations> initialOperations = new ArrayList<>();
        initialOperations.add(AfterPrintOperations.AFTER_PRINT_CUT_PAPER);
        ArrayList<AfterPrintOperations> updatedOperations = new ArrayList<>();
        updatedOperations.add(AfterPrintOperations.AFTER_PRINT_OPEN_CASH_DRAWER);

        // Act
        requestPrint.setAfterPrintOperations(initialOperations);
        requestPrint.setAfterPrintOperations(updatedOperations);
        var result = requestPrint.getAfterPrintOperations();

        // Assert
        assertEquals(updatedOperations, result, "getAfterPrintOperations should return the latest list set");
    }


    @Test
    void testGetPrinterName_WhenPrinterNameIsNull_ShouldReturnNull() {
        // Arrange
        RequestPrint requestPrint = new RequestPrint();

        // Act
        String result = requestPrint.getPrinterName();

        // Assert
        assertNull(result, "getPrinterName should return null when printerName is not set");
    }

    @Test
    void testGetPrinterName_WhenPrinterNameIsSet_ShouldReturnSameValue() {
        // Arrange
        RequestPrint requestPrint     = new RequestPrint();
        String       printerNameValue = "Printer_1";

        // Act
        requestPrint.setPrinterName(printerNameValue);
        String result = requestPrint.getPrinterName();

        // Assert
        assertEquals(printerNameValue, result, "getPrinterName should return the same value that was set");
    }

    @Test
    void testGetPrinterName_WhenPrinterNameIsUpdated_ShouldReturnUpdatedValue() {
        // Arrange
        RequestPrint requestPrint            = new RequestPrint();
        String       initialPrinterNameValue = "Printer_1";
        String       updatedPrinterNameValue = "Printer_2";

        // Act
        requestPrint.setPrinterName(initialPrinterNameValue);
        requestPrint.setPrinterName(updatedPrinterNameValue);
        String result = requestPrint.getPrinterName();

        // Assert
        assertEquals(updatedPrinterNameValue, result, "getPrinterName should return the latest set value");
    }

}
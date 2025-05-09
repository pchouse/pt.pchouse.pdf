package pt.pchouse.pdf.api.request;

import org.junit.jupiter.api.Test;
import pt.pchouse.pdf.api.request.datasource.IDataSource;
import pt.pchouse.pdf.api.sign.ISignPDF;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class RequestReportTest
{

    @Test
    void testGetReport_DefaultConstructor_ReturnsNull() {
        // Arrange
        RequestReport<IDataSource, ISignPDF> requestReport = new RequestReport<>();

        // Act
        String report = requestReport.getReport();

        // Assert
        assertNull(report, "The report should be null by default.");
    }

    @Test
    void testGetReport_CustomConstructor_ReturnsValue() {
        // Arrange
        IDataSource                          mockDataSource = mock(IDataSource.class);
        ISignPDF                             mockSignPDF    = mock(ISignPDF.class);
        RequestReport<IDataSource, ISignPDF> requestReport  = new RequestReport<>(mockDataSource, mockSignPDF);
        String                               expectedReport = "Custom Report";
        requestReport.setReport(expectedReport);

        // Act
        String report = requestReport.getReport();

        // Assert
        assertEquals(expectedReport, report, "The report should match the value set.");
    }

    @Test
    void testSetReport_ValueIsSet_GetterReturnsSameValue() {
        // Arrange
        RequestReport<IDataSource, ISignPDF> requestReport = new RequestReport<>();
        String                               inputReport   = "Sample Report";

        // Act
        requestReport.setReport(inputReport);
        String report = requestReport.getReport();

        // Assert
        assertEquals(inputReport, report, "The report getter should return the same value as set.");
    }

    @Test
    void testSetReport_NullValue_ReturnsNull() {
        // Arrange
        RequestReport<IDataSource, ISignPDF> requestReport = new RequestReport<>();

        // Act
        requestReport.setReport(null);
        String report = requestReport.getReport();

        // Assert
        assertNull(report, "The report should be null when set to null.");
    }

    @Test
    void testSetReport_MultipleUpdates_ReturnsUpdatedValue() {
        // Arrange
        RequestReport<IDataSource, ISignPDF> requestReport = new RequestReport<>();
        String                               firstReport   = "First Report";
        String                               secondReport  = "Updated Report";

        requestReport.setReport(firstReport);
        String reportAfterFirstUpdate = requestReport.getReport();
        assertEquals(firstReport, reportAfterFirstUpdate, "The report should return the first set value.");

        requestReport.setReport(secondReport);
        String reportAfterSecondUpdate = requestReport.getReport();
        assertEquals(secondReport, reportAfterSecondUpdate, "The report should return the updated value.");
    }


    @Test
    void testSetDataSource_ValueIsSet_GetterReturnsSameValue() {
        // Arrange
        RequestReport<IDataSource, ISignPDF> requestReport  = new RequestReport<>();
        IDataSource                          mockDataSource = mock(IDataSource.class);

        // Act
        requestReport.setDataSource(mockDataSource);
        IDataSource dataSource = requestReport.getDataSource();

        // Assert
        assertEquals(mockDataSource, dataSource, "The data source getter should return the same value as set.");
    }

    @Test
    void testSetDataSource_NullValue_ReturnsNull() {
        // Arrange
        RequestReport<IDataSource, ISignPDF> requestReport = new RequestReport<>();

        // Act
        requestReport.setDataSource(null);
        IDataSource dataSource = requestReport.getDataSource();

        // Assert
        assertNull(dataSource, "The data source should be null when set to null.");
    }

    @Test
    void testSetSignPDF_ValueIsSet_GetterReturnsSameValue() {
        // Arrange
        RequestReport<IDataSource, ISignPDF> requestReport = new RequestReport<>();
        ISignPDF                             mockSignPDF   = mock(ISignPDF.class);

        // Act
        requestReport.setSignPDF(mockSignPDF);
        ISignPDF signPDF = requestReport.getSignPDF();

        // Assert
        assertEquals(mockSignPDF, signPDF, "The sign PDF getter should return the same value as set.");
    }

    @Test
    void testSetSignPDF_NullValue_ReturnsNull() {
        // Arrange
        RequestReport<IDataSource, ISignPDF> requestReport = new RequestReport<>();

        // Act
        requestReport.setSignPDF(null);
        ISignPDF signPDF = requestReport.getSignPDF();

        // Assert
        assertNull(signPDF, "The sign PDF should be null when set to null.");
    }


    @Test
    void testGetCopies_DefaultConstructor_ReturnsNull() {
        // Arrange
        RequestReport<IDataSource, ISignPDF> requestReport = new RequestReport<>();

        // Act
        int copies = requestReport.getCopies();

        // Assert
        assertEquals(1, copies, "The copies should be null by default.");
    }

    @Test
    void testSetCopies_CustomValue_GetterReturnsSameValue() {
        // Arrange
        RequestReport<IDataSource, ISignPDF> requestReport  = new RequestReport<>();
        Integer                              expectedCopies = 5;

        // Act
        requestReport.setCopies(expectedCopies);
        Integer copies = requestReport.getCopies();

        // Assert
        assertEquals(expectedCopies, copies, "The copies getter should return the same value as set.");
    }

    @Test
    void testSetEncoding_ValueIsSet_GetterReturnsSameValue() {
        // Arrange
        RequestReport<IDataSource, ISignPDF> requestReport    = new RequestReport<>();
        String                               expectedEncoding = "UTF-8";

        // Act
        requestReport.setEncoding(expectedEncoding);
        String encoding = requestReport.getEncoding();

        // Assert
        assertEquals(expectedEncoding, encoding, "The encoding getter should return the same value as set.");
    }

    @Test
    void testSetEncoding_NullValue_ReturnsNull() {
        // Arrange
        RequestReport<IDataSource, ISignPDF> requestReport = new RequestReport<>();

        // Act
        requestReport.setEncoding(null);
        String encoding = requestReport.getEncoding();

        // Assert
        assertNull(encoding, "The encoding should be null when set to null.");
    }

    @Test
    void testSetMetadata_ValueIsSet_GetterReturnsSameValue() {
        // Arrange
        RequestReport<IDataSource, ISignPDF> requestReport    = new RequestReport<>();
        Metadata                               expectedMetadata = mock(Metadata.class)  ;

        // Act
        requestReport.setMetadata(expectedMetadata);
        Metadata metadata = requestReport.getMetadata();

        // Assert
        assertEquals(expectedMetadata, metadata, "The metadata getter should return the same value as set.");
    }

    @Test
    void testSetMetadata_NullValue_ReturnsNull() {
        // Arrange
        RequestReport<IDataSource, ISignPDF> requestReport = new RequestReport<>();

        // Act
        requestReport.setMetadata(null);
        Metadata metadata = requestReport.getMetadata();

        // Assert
        assertNull(metadata, "The metadata should be null when set to null.");
    }

    @Test
    void testGetPdfProperty_DefaultConstructor_ReturnsNull() {
        // Arrange
        RequestReport<IDataSource, ISignPDF> requestReport = new RequestReport<>();

        // Act
        PdfProperties pdfProperty = requestReport.getPdfProperties();

        // Assert
        assertNull(pdfProperty, "The pdfProperty should be null by default.");
    }

    @Test
    void testSetPdfProperty_ValueIsSet_GetterReturnsSameValue() {
        // Arrange
        RequestReport<IDataSource, ISignPDF> requestReport       = new RequestReport<>();
        PdfProperties                        expectedPdfProperty = mock(PdfProperties.class);

        // Act
        requestReport.setPdfProperties(expectedPdfProperty);
        PdfProperties pdfProperty = requestReport.getPdfProperties();

        // Assert
        assertEquals(
                expectedPdfProperty,
                pdfProperty,
                "The pdfProperty getter should return the same value as set."
        );
    }

    @Test
    void testSetPdfProperty_NullValue_ReturnsNull() {
        // Arrange
        RequestReport<IDataSource, ISignPDF> requestReport = new RequestReport<>();

        // Act
        requestReport.setPdfProperties(null);
        PdfProperties pdfProperty = requestReport.getPdfProperties();

        // Assert
        assertNull(pdfProperty, "The pdfProperty should be null when set to null.");
    }

    @Test
    void testSetPdfProperty_MultipleUpdates_ReturnsUpdatedValue() {
        // Arrange
        RequestReport<IDataSource, ISignPDF> requestReport = new RequestReport<>();

        PdfProperties firstPdfProperty  = mock(PdfProperties.class);
        PdfProperties secondPdfProperty = mock(PdfProperties.class);

        requestReport.setPdfProperties(firstPdfProperty);
        PdfProperties pdfPropertyAfterFirstUpdate = requestReport.getPdfProperties();
        assertEquals(firstPdfProperty, pdfPropertyAfterFirstUpdate, "The pdfProperty should return the first set value.");

        requestReport.setPdfProperties(secondPdfProperty);
        PdfProperties pdfPropertyAfterSecondUpdate = requestReport.getPdfProperties();
        assertEquals(secondPdfProperty, pdfPropertyAfterSecondUpdate, "The pdfProperty should return the updated value.");
    }

    @Test
    void testSetPdfProperty_MetadataUpdates_ReturnsUpdatedValue() {
        // Arrange
        RequestReport<IDataSource, ISignPDF> requestReport = new RequestReport<>();

        Metadata firstMetadataProperty  = mock(Metadata.class);
        Metadata secondMetadataProperty = mock(Metadata.class);

        requestReport.setMetadata(firstMetadataProperty);
        Metadata pdfPropertyAfterFirstUpdate = requestReport.getMetadata();
        assertEquals(firstMetadataProperty, pdfPropertyAfterFirstUpdate, "The pdfProperty should return the first set value.");

        requestReport.setMetadata(secondMetadataProperty);
        Metadata pdfPropertyAfterSecondUpdate = requestReport.getMetadata();
        assertEquals(secondMetadataProperty, pdfPropertyAfterSecondUpdate, "The pdfProperty should return the updated value.");
    }
    
}
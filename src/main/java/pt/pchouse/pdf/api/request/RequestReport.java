package pt.pchouse.pdf.api.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import pt.pchouse.pdf.api.print.AfterPrintOperations;
import pt.pchouse.pdf.api.request.datasource.*;
import pt.pchouse.pdf.api.sign.ISignPDF;
import pt.pchouse.pdf.api.sign.multicert.MulticertSignRequest;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Represents a request for generating a report, which includes details about
 * the report type, data sources, parameters, and additional configurations.
 * This class supports both PDF generation and printing functionalities and enables
 * advanced features such as customizable metadata, multi-format data sources,
 * signing processes, and post-print operations.
 * <p>
 * This class utilizes polymorphic serialization and deserialization for its data
 * source and signing-related fields, allowing for extensibility with various
 * implementations.
 */
@Component
@RequestScope
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class RequestReport<T extends IDataSource, Y extends ISignPDF>
{
     /**
     * @since 1.0.0
     */
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * The type of request
     *
     * @since 1.0.0
     */
    public enum ReportType
    {
        PDF,
        PRINT
    }

    /**
     * The path to JasperReports file (The compiled jrxml)
     *
     * @since 1.0.0
     */
    private String report;

    /**
     * The printer name of the report type is print.
     * If no printer name will be printed to the default
     * @since 1.0.0
     */
    private String printerName = null;

    /**
     * Represents the type of report to be generated, such as PDF or PRINT.
     * This variable determines the format and delivery mechanism of the report.
     *
     * @see ReportType
     *
     * @since 1.0.0
     */
    private ReportType reportType;

    /**
     * A list of parameters associated with the report request. Each parameter
     * contains specific information required for report generation, such as
     * key-value pairs or parameter configurations.
     *
     * @since 1.0.0
     */
    private ArrayList<Parameter> parameters;

    /**
     * The metadata associated with the report request.
     * It can store additional information or attributes
     * related to the document being generated or processed.
     *
     * @since 1.0.0
     */
    private Metadata metadata = null;

    /**
     * Represents the data source from which the report data is fetched.
     * The data source can be one of several types, including database,
     * JSON over HTTP/HTTPS, or XML over HTTP/HTTPS.
     * <p>
     * This field is serialized and deserialized using polymorphic type
     * handling based on the "type" property, enabling specific handling
     * for different data source implementations.
     *
     * @since 1.0.0
     */
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
    @JsonSubTypes({
            @JsonSubTypes.Type(value = Database.class, name = "database"),
            @JsonSubTypes.Type(value = JsonHttp.class, name = "json-http"),
            @JsonSubTypes.Type(value = JsonHttps.class, name = "json-https"),
            @JsonSubTypes.Type(value = XmlHttp.class, name = "xml-http"),
            @JsonSubTypes.Type(value = XmlHttps.class, name = "xml-https"),
    })
    private IDataSource dataSource;

    /**
     * Represents the PDF signing process for the associated report. This variable
     * holds an instance of a class implementing the {@link ISignPDF} interface,
     * which defines the required behavior for signing documents. Depending on the
     * assigned implementation, different signing providers or strategies can be
     * used.
     * <p>
     * The JSON annotations (`@JsonTypeInfo` and `@JsonSubTypes`) define how this
     * variable is serialized and deserialized when interfacing with external systems,
     * allowing inheritance-based polymorphism. The specific JSON type property is
     * identified as "type", supporting extensions such as {@link MulticertSignRequest}.
     *
     */
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
    @JsonSubTypes({
            @JsonSubTypes.Type(value = MulticertSignRequest.class, name = "multicert"),
    })
    private ISignPDF signPDF = null;

    /**
     * Number of report copies
     *
     * @since 1.0.0
     */
    private int copies = 1;

    /**
     * The report encoding
     *
     * @since 1.0.0
     */
    private String encoding = "UTF-8";

    /**
     * Pdf properties
     *
     * @since 1.0.0
     */
    private PdfProperties pdfProperties;
    /**
     * Represents a list of operations to be executed immediately after a print job is completed.
     * Each operation in the list is defined as an instance of the {@link AfterPrintOperations} enumeration.
     * The list can include actions such as cutting paper or opening a cash drawer, depending on the
     * requirements of the printing process.
     *
     * @since 1.0.0
     */
    private ArrayList<AfterPrintOperations> afterPrintOperations = new ArrayList<>();

    /**
     * Report request definition
     *
     * @since 1.0.0
     */
    public RequestReport() {
        logger.debug("New instance of {}", this.getClass().getName());
    }

    /**
     * Report request definition
     *
     * @since 1.0.0
     */
    public RequestReport(T dataSource, Y signPDF) {
        logger.debug(
                "New instance of {} with datasource {} and sign {}",
                this.getClass().getName(),
                dataSource == null ? "null" : dataSource.getClass().getName(),
                signPDF == null ? "null" : signPDF.getClass().getName()
        );
        this.dataSource = dataSource;
        this.signPDF    = signPDF;
    }

    /**
     * The  jasper report path (jasper file compiled from jrxml file)
     *
     * @return The base64 encoded jasper report
     * @since 1.0.0
     */
    public String getReport() {
        return report;
    }

    /**
     * The jasper report path (jasper file compiled from jrxml file)
     *
     * @param report The base64 encoded jasper report
     * @since 1.0.0
     */
    public void setReport(String report) {
        this.report = report;
        logger.debug("Report was set");
    }

    /**
     * The type of report that should be generated.
     *
     * @return The type
     * @since 1.0.0
     */
    public ReportType getReportType() {
        return reportType;
    }

    /**
     * The type of report that should be generated.
     *
     * @param reportType The type
     * @since 1.0.0
     */
    public void setReportType(ReportType reportType) {
        this.reportType = reportType;
        logger.debug("ReportType set to {}", this.reportType == null ? "null" : this.reportType.toString());
    }

    /**
     * The report parameters
     *
     * @return The list of parameters
     * @since 1.0.0
     */
    public ArrayList<Parameter> getParameters() {
        return parameters;
    }

    /**
     * The report parameters
     *
     * @param parameters The list of parameters
     * @since 1.0.0
     */
    public void setParameters(ArrayList<Parameter> parameters) {
        this.parameters = parameters;
        logger.debug("Parameters set to {}", this.parameters == null ? "null" : "an list");
    }


    /**
     * Get the number of report copies
     *
     * @return The number of copies
     * @since 1.0.0
     */
    public int getCopies() {
        return copies;
    }

    /**
     * Set the number of report copies
     *
     * @param copies The number of copies
     * @since 1.0.0
     */
    public void setCopies(int copies) {
        this.copies = copies;
        logger.debug("Number of report copies set to {}", this.copies);
    }

    /**
     * Get the report encoding
     *
     * @return The encoding
     * @since 1.0.0
     */
    public String getEncoding() {
        return encoding;
    }

    /**
     * set the report encoding
     *
     * @param encoding The encoding
     * @since 1.0.0
     */
    public void setEncoding(String encoding) {
        this.encoding = encoding;
        logger.debug("Report encoding set to {}", this.encoding);
    }

    /**
     * Gets the list of operations to be performed immediately
     * after the completion of a print job.
     *
     * @return The list of after-print operations.
     *
     * @since 1.0.0
     */
    public ArrayList<AfterPrintOperations> getAfterPrintOperations() {
        return afterPrintOperations;
    }

    /**
     * Sets the list of operations to be performed immediately after
     * the completion of a print job.
     *
     * @param afterPrintOperations A list of after-print operations.
     *
     * @since 1.0.0
     */
    public void setAfterPrintOperations(ArrayList<AfterPrintOperations> afterPrintOperations) {
        this.afterPrintOperations = afterPrintOperations;
    }

    /**
     * Document metadata
     *
     * @return The metadata
     * @since 1.0.0
     */
    public Metadata getMetadata() {
        return metadata;
    }

    /**
     * Document metadata
     *
     * @param metadata The metadata
     * @since 1.0.0
     */
    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    /**
     * Get the pdf properties
     *
     * @return Properties
     * @since 1.0.0
     */
    public PdfProperties getPdfProperties() {
        return pdfProperties;
    }

    /**
     * Set The pdf properties
     *
     * @param pdfProperties The properties
     * @since 1.0.0
     */
    public void setPdfProperties(PdfProperties pdfProperties) {
        this.pdfProperties = pdfProperties;
    }

    /**
     * @return datasource
     * @since 1.0.0
     */
    public IDataSource getDataSource() {
        return dataSource;
    }

    /**
     * @param dataSource The datasource
     * @since 1.0.0
     */
    public void setDataSource(IDataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * @return PDF signing provider
     * @since 1.0.0
     */
    public ISignPDF getSignPDF() {
        return signPDF;
    }

    /**
     * @param signPDF PDF signing provider
     * @since 1.0.0
     */
    public void setSignPDF(ISignPDF signPDF) {
        this.signPDF = signPDF;
    }

    /**
     *
     * @return The printer name
     * @since 1.0.0
     */
    public String getPrinterName() {
        return printerName;
    }

    /**
     *
     * @param printerName The printer name
     * @since 1.0.0
     */
    public void setPrinterName(String printerName) {
        this.printerName = printerName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        @SuppressWarnings("rawtypes") RequestReport request = (RequestReport) o;
        return copies == request.copies
                && afterPrintOperations == request.afterPrintOperations
                && Objects.equals(report, request.report)
                && reportType == request.reportType
                && Objects.equals(parameters, request.parameters)
                && Objects.equals(encoding, request.encoding)
                && Objects.equals(metadata, request.metadata)
                && Objects.equals(dataSource, request.dataSource)
                && Objects.equals(signPDF, request.signPDF)
                && Objects.equals(printerName, request.printerName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                report,
                reportType,
                parameters,
                copies,
                encoding,
                afterPrintOperations,
                metadata
        );
    }
}

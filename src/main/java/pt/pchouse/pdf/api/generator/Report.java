package pt.pchouse.pdf.api.generator;

import com.lowagie.text.pdf.PdfWriter;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.type.OrientationEnum;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePrintServiceExporterConfiguration;
import net.sf.jasperreports.pdf.SimplePdfExporterConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pt.pchouse.pdf.api.request.Metadata;
import pt.pchouse.pdf.api.request.Parameter;
import pt.pchouse.pdf.api.request.RequestReport;
import pt.pchouse.pdf.api.request.datasource.*;
import net.sf.jasperreports.json.data.JsonDataSource;

import java.io.*;
import java.math.BigDecimal;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import net.sf.jasperreports.pdf.JRPdfExporter;
import pt.pchouse.pdf.api.sign.ISignPDF;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.*;
import javax.print.attribute.standard.OrientationRequested;
import javax.print.attribute.standard.PrinterName;

/**
 * The Report class provides functionality to generate, export, print, and manage
 * reports with support for handling different data sources and formats. It includes methods
 * for synchronous and asynchronous operations, handling metadata, and managing resources.
 *
 * @since 1.0.0
 */
@Component
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
@PropertySources({
        @PropertySource("classpath:application.properties"),
        @PropertySource(value = "file:${apphome}/application.properties", ignoreResourceNotFound = true)
})
public class Report
{
    /**
     * @since 1.0.0
     */
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * A variable representing the data source connection used for report generation.
     * <p>
     * The `connectionDatasource` can hold various types of data source objects, such as a database
     * connection, a remote data source, or other resource connections. Its type is `Object` to allow
     * flexibility in supporting different kinds of data sources.
     * <p>
     * This variable is used throughout the report generation process to fetch data required for
     * creating reports. It serves as the primary point of reference for accessing data, whether from
     * a local database, a remote server, or another source.
     * <p>
     * Proper management of the `connectionDatasource` is crucial, as it may involve open connections
     * or allocated resources. The `dispose` method ensures that resources associated with this
     * object are safely released when no longer needed.
     */
    private Object connectionDatasource = null;

    /**
     * Represents the base directory path for jasper report files.
     * This variable is populated using the `report.basedir` property from the application configuration.
     * It serves as a reference point for locating report files.
     *
     * @since 1.0.0
     */
    @Value("${report.basedir}")
    private String REPORT_BASE_PATH;

    /**
     * Represents a request object that combines a data source and a PDF signing service.
     * This variable is immutable and is used to define the input and processing operations
     * required for report generation and potential PDF signing.
     * <p>
     * The data source {@link IDataSource} is responsible for providing the necessary data
     * for the report. The {@link ISignPDF} interface enables PDF-specific operations such
     * as signing.
     * <p>
     * This variable is critical in configuring the report generation and export functionality
     * by encapsulating the context required for processing.
     *
     * @since 1.0
     */
    private final RequestReport<IDataSource, ISignPDF> request;

    /**
     * Constructs a Report instance with the specified request configuration.
     * Initializes the report object with the given request and logs the creation process.
     *
     * @param request The request configuration used to generate the report.
     *                It includes the data source and PDF signing details.
     *
     * @since 1.0.0
     */
    public Report(RequestReport<IDataSource, ISignPDF> request) {
        logger.info("[{}][{}] Starting Report constructor for class {}",
                Thread.currentThread().getName(),
                System.currentTimeMillis(),
                this.getClass().getName());
        logger.debug("Report base path: {}", REPORT_BASE_PATH);
        this.request = request;
        logger.info("Report instance created successfully");
    }

    /**
     * Asynchronously exports a report based on the specified request configuration.
     * This method enables non-blocking execution of the export process by running it in a separate thread.
     *
     * @return A CompletableFuture that completes with a Base64-encoded PDF string if the report is of type PDF,
     * or completes with null if the report is printed. The CompletableFuture will complete exceptionally
     * if an error occurs during the export process.
     * @since 1.0.0
     */
    public CompletableFuture<String> exportAsync() {
        logger.info("Entering exportAsync method: starting asynchronous export process");
        return CompletableFuture.supplyAsync(() -> {
            try {
                String result = export();
                logger.info("Successfully completed asynchronous export process");
                return result;
            } catch (Exception e) {
                logger.error("[{}][{}] Error in exportAsync: {}",
                        Thread.currentThread().getName(),
                        System.currentTimeMillis(),
                        e.getMessage(), e);
                throw new CompletionException(e);
            }
        });
    }

    /**
     * Exports a report based on the specified request configuration. If the report type is PDF,
     * the report is exported as a Base64-encoded PDF string. Otherwise, the report is printed.
     *
     * @return A Base64-encoded PDF string if the report is of type PDF, otherwise null if the report
     * is printed.
     * @throws Exception If an error occurs during report generation, export, or printing process.
     */
    public String export() throws Exception {
        logger.info("Entering export method: processing report export");
        if (request.getReportType() == RequestReport.ReportType.PDF) {
            logger.debug("Report type is PDF. Proceeding with Base64 encoding");
            String result = this.exportBase64Encoded();
            logger.info("Successfully exported the report as Base64 encoded string");
            return result;
        }

        logger.debug("Report type is not PDF. Proceeding to print the report");
        this.printReport();
        logger.info("Successfully printed the report");
        return null;
    }

    /**
     * Asynchronously prints a report based on the provided request configuration.
     * This method executes the printing process in a non-blocking manner by running it in a separate thread.
     * Any exceptions encountered during the execution are logged and propagated.
     *
     * @return A CompletableFuture that completes normally with a {@code null} result when the report is successfully printed,
     * or completes exceptionally if an error occurs during the report generation or printing process.
     * @since 1.0.0
     */
    @SuppressWarnings("unused")
    public CompletableFuture<Void> printReportAsync() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                printReport();
            } catch (Exception e) {
                logger.error("Error printing report", e);
                throw new CompletionException(e);
            }
            return null;
        });
    }

    /**
     * Prints a report based on the provided request configuration and sends it to the printer.
     *
     * @throws Exception If an error occurs during the report generation, export, or print process.
     * @since 1.0
     */
    public void printReport() throws Exception {
        logger.info("Entering printReport method: starting report printing process");
        try {
            logger.debug("Initializing printer configurations and exporter settings");
            PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
            PrintServiceAttributeSet printServiceAttributeSet = new HashPrintServiceAttributeSet();

            SimplePrintServiceExporterConfiguration configuration = new SimplePrintServiceExporterConfiguration();

            configuration.setPrintRequestAttributeSet(printRequestAttributeSet);
            configuration.setPrintServiceAttributeSet(printServiceAttributeSet);

            logger.debug("Creating exporter input");
            SimpleExporterInput simpleExporterInput = this.simpleExporterInputFactory();

            printRequestAttributeSet.add(
                    simpleExporterInput.getItems().getFirst().getJasperPrint().getOrientation() == OrientationEnum.LANDSCAPE ?
                            OrientationRequested.LANDSCAPE : OrientationRequested.PORTRAIT
            );

            if (request.getPrinterName() == null && request.getPrinterName().trim().isEmpty()) {
                logger.debug("Printer name is not specified. Using default printer");
                PrintService service = PrintServiceLookup.lookupDefaultPrintService();
                printServiceAttributeSet.add(new PrinterName(service.getName(), null));
            } else {
                logger.debug("Printer name specified as {}", request.getPrinterName());
                printServiceAttributeSet.add(new PrinterName(request.getPrinterName().trim(), null));
            }

            JRPrintServiceExporter exporter = new JRPrintServiceExporter();
            exporter.setExporterInput(simpleExporterInput);
            exporter.setConfiguration(configuration);

            exporter.exportReport();
            logger.info("Report printing process completed successfully");
        } catch (Exception e) {
            logger.error("Error occurred during printReport method execution", e);
            throw e;
        } finally {
            logger.debug("Cleaning up resources in printReport method");
            this.dispose();
        }
    }

    /**
     * Asynchronously exports the report as a byte array in PDF format.
     * This method uses a non-blocking approach by executing the export process in a separate thread.
     * Any exceptions encountered during execution are logged and propagated as a CompletionException.
     *
     * @return A CompletableFuture containing a byte array representing the exported PDF report.
     * The CompletableFuture will complete exceptionally if an error occurs during the export process.
     * @since 1.0.0
     */
    public CompletableFuture<byte[]> exportAsByteArrayAsync() {
        return CompletableFuture.supplyAsync(() -> {
            logger.info("Starting exportAsByteArrayAsync - exporting report as byte array in PDF format");
            try {
                byte[] result = exportAsByteArray();
                logger.info("exportAsByteArrayAsync completed successfully");
                return result;
            } catch (Exception e) {
                logger.error("Error occurred during exportAsByteArrayAsync execution", e);
                throw new CompletionException(e);
            }
        });
    }

    /**
     * Exports the report as a byte array in PDF format.
     * <p>
     * This method generates the report using the JasperReports library and
     * exports it as a PDF byte array. It manages resources such as output streams
     * and exporter inputs internally and ensures proper cleanup after execution.
     *
     * @return A byte array containing the exported PDF report.
     * @throws Exception If an error occurs during the export process.
     * @since 1.0
     */
    public byte[] exportAsByteArray() throws Exception {
        try {

            logger.info("Starting exportAsByteArray - exporting report to PDF byte array");

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            logger.debug("Initialized ByteArrayOutputStream");

            SimpleOutputStreamExporterOutput streamExporterOutput = new SimpleOutputStreamExporterOutput(
                    byteArrayOutputStream
            );
            logger.debug("Created SimpleOutputStreamExporterOutput");

            SimpleExporterInput simpleExporterInput = this.simpleExporterInputFactory();
            logger.debug("Generated SimpleExporterInput");

            JRPdfExporter jrPdfExporter = new JRPdfExporter();
            jrPdfExporter.setExporterInput(simpleExporterInput);
            jrPdfExporter.setExporterOutput(streamExporterOutput);
            logger.debug("Configured JRPdfExporter with inputs and outputs");

            this.applyMetadata(jrPdfExporter);
            logger.debug("Applied metadata to the JRPdfExporter");

            jrPdfExporter.exportReport();
            logger.info("Report exportToByteArray completed successfully");

            byteArrayOutputStream.flush();

            byte[] bytes = byteArrayOutputStream.toByteArray();
            logger.debug("Converted output stream to byte array");
            byteArrayOutputStream.close();

            return bytes;

        } catch (Exception e) {
            logger.error("Error occurred during exportAsByteArray execution", e);
            throw e;
        } finally {
            this.dispose();
        }
    }

    /**
     * Exports a report as a PDF and returns the generated PDF as a Base64-encoded string.
     *
     * @throws Exception If an error occurs during the PDF generation or export process.
     * @since 1.0
     */
    public String exportBase64Encoded() throws Exception {
        logger.info("Starting exportBase64Encoded - converting PDF byte array to Base64-encoded string");
        String base64Encoded = Base64.getEncoder().encodeToString(this.exportAsByteArray());
        logger.info("Successfully completed exportBase64Encoded - Encoded String Length={}", base64Encoded.length());
        return base64Encoded;
    }

    /**
     * Creates a {@link SimpleExporterInput} for exporting a JasperReports report based on the given request.
     * This method normalizes and validates the report path, prepares report parameters, and fills the report
     * with data from the specified data source.
     *
     * @return A {@link SimpleExporterInput} instance initialized with the filled JasperPrint objects for exporting.
     * @throws Exception If the report path is invalid, the specified data source type is unsupported, the report
     *                   file does not exist, or an error occurs during the report generation process.
     * @since 1.0
     */
    SimpleExporterInput simpleExporterInputFactory()
            throws Exception
    {
        logger.info("Starting simpleExporterInputFactory - creating exporter input");

        logger.debug("Validating and normalizing report file path");

        if (request.getReport() == null || request.getReport().trim().isEmpty()) {
            logger.error("Report file path is null or empty");
            throw new Exception("Report file path is null or empty");
        }

        if (!request.getReport().endsWith(".jasper")) {
            logger.error("Report file path is not a jasper report file (does not end with .jasper) {}", request.getReport());
            throw new Exception("Report file path is not a jasper report file (does not end with .jasper)");
        }

        Path jasperReportPath = Paths.get(REPORT_BASE_PATH, request.getReport()).normalize().toAbsolutePath();

        if (!jasperReportPath.startsWith(REPORT_BASE_PATH)) {
            logger.error(
                    "Normalized jasper report path {} is not in the base path {}",
                    jasperReportPath,
                    REPORT_BASE_PATH
            );
            throw new Exception("Normalized jasper report path is not in the base path");
        }

        if (!jasperReportPath.toFile().exists()) {
            logger.error("Jasper report file does not exist {}", jasperReportPath);
            throw new Exception("Jasper report file does not exist");
        }

        logger.debug("Report file path validated: {}", jasperReportPath);

        logger.trace("Parsing report parameters");
        Map<String, Object>  reportParameters = this.parseReportParameters();
        logger.debug("Parsed parameters: {}", reportParameters);

        logger.trace("Processing datasource and initializing JasperPrint objects");
        IDataSource dataSource     = request.getDataSource();
        String      datasourceName = dataSource.getClass().getSimpleName();

        Object jrDatasource = switch (datasourceName) {
            case "Database" -> this.getDtabaseConnection();
            case "JsonHttp", "JsonHttps", "XmlHttp",
                 "XmlHttps" -> this.getRemoteDataSource();
            default -> {
                logger.error("Unknown datasource type: {}", datasourceName);
                throw new Exception("Unknown datasource type");
            }
        };

        ArrayList<JasperPrint> jasperPrints = new ArrayList<>();

        for (int index = 1; index <= request.getCopies(); index++) {

            reportParameters.put("RR_INDEX_PARAMETER", index);
            logger.debug("Processing copy {} with parameters {}", index, reportParameters);

            JasperPrint jasperPrint = datasourceName.equalsIgnoreCase("database") ?
                    net.sf.jasperreports.engine.JasperFillManager.fillReport(
                            jasperReportPath.toFile().getAbsolutePath(),
                            reportParameters,
                            (Connection) jrDatasource
                    )
                    :
                    net.sf.jasperreports.engine.JasperFillManager.fillReport(
                            jasperReportPath.toFile().getAbsolutePath(),
                            reportParameters,
                            (JRDataSource) jrDatasource
                    );

            jasperPrints.add(jasperPrint);
        }

        SimpleExporterInput simpleExporterInput = SimpleExporterInput.getInstance(jasperPrints);

        logger.info("Successfully created SimpleExporterInput");
        return simpleExporterInput;
    }

    /**
     * Parses the provided list of report parameters and converts them into a map of key-value pairs
     * based on the parameter type and its corresponding value.
     *
     * @return A map containing the parsed parameters with their names as keys and their parsed values as values.
     * @throws ParseException If a parameter value cannot be parsed due to invalid format,
     *                        unsupported type, or other errors during parsing.
     * @since 1.0
     */
    public Map<String, Object> parseReportParameters() throws ParseException {

        HashMap<String, Object> reportParameters = new HashMap<>();

        for (Parameter param : request.getParameters()) {
            String name  = param.getName();
            String value = param.getValue();
            logger.debug("Processing parameter: Name={} Type={} value={}", name, param.getType(), value);
            switch (param.getType()) {
                case Parameter.Types.P_STRING:
                    reportParameters.put(name, value);
                    break;
                case Parameter.Types.P_BOOLEAN:
                case Parameter.Types.P_BOOL:
                    try {
                        reportParameters.put(name, Util.parseBool(value));
                    } catch (Exception e) {
                        logger.error("Error parsing P_BOOLEAN/P_BOOL parameter - Name={} Value={}", name, value, e);
                        throw new ParseException(e.getMessage());
                    }
                    break;
                case Parameter.Types.P_DOUBLE:
                    reportParameters.put(name, Double.valueOf(value));
                    break;
                case Parameter.Types.P_FLOAT:
                    reportParameters.put(name, Float.valueOf(value));
                    break;
                case Parameter.Types.P_INTEGER:
                    reportParameters.put(name, Integer.valueOf(value));
                    break;
                case Parameter.Types.P_LONG:
                    reportParameters.put(name, Long.valueOf(value));
                    break;
                case Parameter.Types.P_SHORT:
                    reportParameters.put(name, Short.valueOf(value));
                    break;
                case Parameter.Types.P_BIGDECIMAL:
                    reportParameters.put(name, BigDecimal.valueOf(Double.parseDouble(value)));
                    break;
                case Parameter.Types.P_DATE:
                    SimpleDateFormat sdf = new SimpleDateFormat(param.getFormat());
                    try {
                        reportParameters.put(name, sdf.parse(value));
                    } catch (java.text.ParseException e) {
                        throw new ParseException(e.getMessage());
                    }
                    break;
                case Parameter.Types.P_TIME:
                    logger.error("Parsing P_TIME parameter is not implemented yet");
                    throw new ParseException("Not implemented");
//                    break;
                case Parameter.Types.P_SQL_TIME:
                    reportParameters.put(name, new java.sql.Time(Long.parseLong(value)));
                    break;
                case Parameter.Types.P_SQL_DATE:
                    reportParameters.put(name, new java.sql.Date(Long.parseLong(value)));
                    break;
                case Parameter.Types.P_TIMESTAMP:
                    reportParameters.put(name, new java.sql.Timestamp(Long.parseLong(value)));
                    break;
                default:
                    logger.error("Unknown parameters type '{}'", param.getType());
                    throw new ParseException("Unknown parameters type '" + param.getType() + "'");
            }
        }

        return reportParameters;
    }

    /**
     * Establishes a database connection using the provided data source details.
     * Attempts to load the database driver and create a connection using the
     * connection string, username, and password specified in the {@link Database} object.
     *
     * @return A {@link Connection} object representing the active database connection.
     * @throws Exception If the driver class cannot be loaded or any error occurs during
     *                   the connection establishment process.
     *
     * @since 1.0
     */
    public Connection getDtabaseConnection() throws Exception {
        logger.trace("Load driver class");

        Database dataSource = (Database) request.getDataSource();

        logger.debug("Create db connection {}", dataSource.toString());
        
        this.connectionDatasource = DriverManager.getConnection(
                dataSource.getJdbcConnectionString(),
                dataSource.getUser(),
                dataSource.getPassword()
        );

        return (Connection) this.connectionDatasource;
    }

    /**
     * Retrieves a remote data source and returns it as a JRDataSource object.
     * The method establishes a connection to the remote data source, fetches the input stream,
     * and initializes the appropriate data source type (e.g., JSON or XML).
     *
     * @return An instance of JRDataSource initialized with the data obtained from the remote data source.
     * @throws JRException        If an error occurs related to JasperReports.
     * @throws IOException        If an I/O error occurs during the data fetching process.
     * @throws URISyntaxException If the URI of the remote data source is malformed or invalid.
     *
     * @since 1.0
     */
    public JRDataSource getRemoteDataSource() throws
            JRException, IOException, URISyntaxException
    {
        IRemoteDatasource dataSource = (IRemoteDatasource) request.getDataSource();

        HttpURLConnection connection  = this.httpURLConnection();
        InputStream       inputStream = this.getInputStream(connection);
        return switch (dataSource.getClass().getSimpleName()) {
            case "JsonHttp", "JsonHttps" -> new JsonDataSource(inputStream);
            case "XmlHttp", "XmlHttps" -> new JRXmlDataSource(inputStream);
            default -> throw new JRException("Unknown datasource type");
        };
    }

    /**
     * Creates and configures an HttpURLConnection using the specified server's URL and request type.
     * The method ensures that the protocol matches the expected one (HTTP or HTTPS) based on the server class name.
     *
     * @return An initialized HttpURLConnection configured with the server's details.
     * @throws URISyntaxException If the server's URL is malformed or invalid.
     * @throws IOException        If an I/O error occurs while opening the connection or if the URL protocol is invalid.
     *
     * @since 1.0
     */
    public HttpURLConnection httpURLConnection() throws URISyntaxException, IOException {

        AServer server = (AServer) request.getDataSource();

        logger.debug("Start URL connection");
        java.net.URL      url        = (new URI(server.getUrl())).toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(server.getType().toString());
        connection.setDoInput(true);
        connection.setDoOutput(true);

        if (server.getClass().getSimpleName().toLowerCase().contains("https")) {
            if (!url.getProtocol().equalsIgnoreCase("https")) {
                logger.error("Protocol must be https");
                throw new IOException("Protocol must be https");
            }
        } else {
            if (!url.getProtocol().equalsIgnoreCase("http")) {
                logger.error("Protocol must be http");
                throw new IOException("Protocol must be http");
            }
        }

        this.connectionDatasource = connection;

        return connection;
    }

    /**
     * Retrieves an input stream from an established HttpURLConnection, and if the request method is POST,
     * includes the specified parameters in the request body. The method establishes the connection to the server
     * and returns the resulting input stream.
     *
     * @param httpUrLConnection The HttpURLConnection object that represents the connection to be established.
     * @return An InputStream that can be used to read the data returned by the HTTP connection.
     * @throws IOException If an I/O error occurs during the connection setup or input stream retrieval.
     *
     * @since 1.0
     */
    public InputStream getInputStream(HttpURLConnection httpUrLConnection) throws IOException {
        logger.debug("Start getting connection input stream");

        ArrayList<Parameter> parameters = request.getParameters();

        if (httpUrLConnection.getRequestMethod().equalsIgnoreCase("POST")) {
            logger.trace("Setting parameters in the POST message body");
            if (!parameters.isEmpty()) {
                StringBuilder result = new StringBuilder();
                parameters.forEach((parameter) -> {
                    result.append(URLEncoder.encode(parameter.getName(), StandardCharsets.UTF_8));
                    result.append("=");
                    result.append(URLEncoder.encode(parameter.getValue(), StandardCharsets.UTF_8));
                    result.append("&");
                });
                logger.debug("HTTP POST parameters set: {}", result);

                OutputStream   os;
                BufferedWriter writer;
                os     = httpUrLConnection.getOutputStream();
                writer = new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8));
                writer.write(result.toString());
                writer.flush();
                writer.close();
                os.close();
                logger.debug("[{}][{}] Successfully wrote HTTP POST parameters to output stream: {}",
                        Thread.currentThread().getName(),
                        System.currentTimeMillis(),
                        parameters);
            }
        }

        logger.debug("Start connection");
        httpUrLConnection.connect();
        logger.trace("[{}][{}] Returning InputStream from HTTP connection",
                Thread.currentThread().getName(),
                System.currentTimeMillis());
        return httpUrLConnection.getInputStream();
    }

    /**
     * Retrieves the base path used for report generation.
     *
     * @return A string representing the base path for reports.
     * @since 1.0
     */
    @SuppressWarnings("unused")
    public String getReportBasePath() {
        return REPORT_BASE_PATH;
    }

    /**
     * Sets the base path for report generation.
     *
     * @param reportBasePath The base path to be used for reports.
     * @since 1.0
     */
    public void setReportBasePath(String reportBasePath) {
        this.REPORT_BASE_PATH = reportBasePath;
    }

    /**
     * Applies metadata configuration to the PDF exporter based on the provided metadata.
     * This method retrieves the metadata from the request, verifies if the report type
     * is PDF, and sets relevant metadata properties in the exporter configuration.
     *
     * @param jrPdfExporter The JRPdfExporter object to which metadata configurations are applied.
     *
     * @since 1.0
     */
    public void applyMetadata(JRPdfExporter jrPdfExporter) {

        Metadata metadata = request.getMetadata();

        if (metadata == null || request.getReportType() != RequestReport.ReportType.PDF) {
            logger.info("No metadata provided or report type is not PDF. Skipping metadata application.");
            return;
        }

        logger.info("Applying metadata to the PDF report.");

        SimplePdfExporterConfiguration exporterConfig = new SimplePdfExporterConfiguration();

        String author = Util.isNullOrBlank(metadata.getAuthor()) ? "" : metadata.getAuthor();
        exporterConfig.setMetadataAuthor(author);
        logger.debug("Metadata Author set to: {}", author);

        String creator = Util.isNullOrBlank(metadata.getCreator()) ? "" : metadata.getCreator();
        exporterConfig.setMetadataCreator(creator);
        logger.debug("Metadata Creator set to: {}", creator);

        String keywords = Util.isNullOrBlank(metadata.getKeywords()) ? "" : metadata.getKeywords();
        exporterConfig.setMetadataKeywords(keywords);
        logger.debug("Metadata Keywords set to: {}", keywords);

        String subject = Util.isNullOrBlank(metadata.getSubject()) ? "" : metadata.getSubject();
        exporterConfig.setMetadataSubject(subject);
        logger.debug("Metadata Subject set to: {}", subject);

        String title = Util.isNullOrBlank(metadata.getTitle()) ? "" : metadata.getTitle();
        exporterConfig.setMetadataTitle(title);
        logger.debug("Metadata Title set to: {}", title);

        boolean displayTitle = metadata.isDisplayMetadataTitle();
        exporterConfig.setDisplayMetadataTitle(displayTitle);
        logger.debug("Metadata Display Title set to: {}", displayTitle);

        if (!Util.isNullOrBlank(metadata.getJavascript())) {
            String javascript = metadata.getJavascript();
            exporterConfig.setPdfJavaScript(javascript);
            logger.debug("PDF JavaScript set.");
        }

        int permissions = this.getPdfPermissions();
        if (permissions != 0) {
            exporterConfig.setPermissions(permissions);
            logger.debug("PDF Permissions set to: {}", permissions);
        }

        if (!Util.isNullOrBlank(metadata.getOwnerPassword())) {
            exporterConfig.setOwnerPassword(metadata.getOwnerPassword());
            exporterConfig.set128BitKey(true);
            exporterConfig.setEncrypted(true);
            logger.debug("PDF Owner Password set and encryption enabled with 128-bit key.");
        }

        if (!Util.isNullOrBlank(metadata.getUserPassword())) {
            exporterConfig.setUserPassword(metadata.getUserPassword());
            logger.debug("PDF User Password set.");
        }

        jrPdfExporter.setConfiguration(exporterConfig);

    }

    /**
     * Retrieves the PDF permissions based on metadata provided in the request object.
     * The permissions are determined by checking various flags in the Metadata object.
     *
     * @return an integer representing the combined PDF permissions flags. If the metadata is unavailable, returns 0.
     *
     * @since 1.0.0
     */
    private int getPdfPermissions() {

        if (request.getMetadata() == null) return 0;

        Metadata metadata = request.getMetadata();

        int permissions = 0;

        if (metadata.isAllowPrinting()) {
            permissions |= PdfWriter.ALLOW_PRINTING;
        }

        if (metadata.isAllowModifying()) {
            permissions |= PdfWriter.ALLOW_MODIFY_CONTENTS;
        }

        if (metadata.isAllowCopying()) {
            permissions |= PdfWriter.ALLOW_COPY;
        }

        if (metadata.isAllowFillIn()) {
            permissions |= PdfWriter.ALLOW_FILL_IN;
        }

        if (metadata.isAllowScreenReaders()) {
            permissions |= PdfWriter.ALLOW_SCREENREADERS;
        }

        if (metadata.isAllowAssembly()) {
            permissions |= PdfWriter.ALLOW_ASSEMBLY;
        }

        if (metadata.isAllowDegradedPrinting()) {
            permissions |= PdfWriter.ALLOW_DEGRADED_PRINTING;
        }

        if (metadata.isAllowModifyAnnotation()) {
            permissions |= PdfWriter.ALLOW_MODIFY_ANNOTATIONS;
        }
        return permissions;
    }

    /**
     * Releases resources associated with the `connectionDatasource`.
     * If the `connectionDatasource` is a database connection (`Connection`),
     * this method checks if it is open and closes it safely if not already closed.
     * If the `connectionDatasource` is an HTTP connection (`HttpURLConnection`),
     * it disconnects the connection.
     * <p>
     * Any exceptions encountered during the disposal process are logged.
     *
     * @since 1.0.0
     */
    public void dispose()
    {
        try {
            logger.trace("Start disposing class resources");

            if (this.connectionDatasource instanceof Connection) {
                logger.trace("Check if database connection closed");

                if (!((Connection) this.connectionDatasource).isClosed()) {
                    logger.trace("Database connection is not closed, going to close it");
                    ((Connection) this.connectionDatasource).close();
                    logger.debug("[{}][{}] Database connection closed successfully",
                            Thread.currentThread().getName(),
                            System.currentTimeMillis());
                } else {
                    logger.debug("Database connection was closed");
                }
                return;
            }

            if (this.connectionDatasource instanceof HttpURLConnection) {
                logger.trace("Check if http connection closed");
                ((HttpURLConnection) this.connectionDatasource).disconnect();
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

}

package pt.pchouse.pdf.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.pchouse.pdf.api.image.ConvertToImage;
import pt.pchouse.pdf.api.image.PdfToImageResponse;
import pt.pchouse.pdf.api.image.RequestPdfToImage;
import pt.pchouse.pdf.api.auth.IAuth;
import pt.pchouse.pdf.api.generator.Report;
import pt.pchouse.pdf.api.print.AfterPrintOperations;
import pt.pchouse.pdf.api.print.Printer;
import pt.pchouse.pdf.api.request.RequestPrint;
import pt.pchouse.pdf.api.request.RequestReport;
import pt.pchouse.pdf.api.request.RequestSign;


import javax.print.PrintService;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * Controller class that manages request endpoints for the application.
 * Handles incoming requests, processes them asynchronously, and provides
 * appropriate responses.
 * <p>
 * This controller is designed to manage report generation requests, validate
 * client authorization, and respond with a generated report or an error
 * message.
 *
 * @RestController Indicates that this class is a Spring REST Controller.
 * @since 1.0.0
 */
@RestController
@PropertySources({
        @PropertySource("classpath:application.properties"),
        @PropertySource(value = "file:${apphome}/application.properties", ignoreResourceNotFound = true)
})
public class Controller
{
    /**
     * @since 1.0.0
     */
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * @since 1.0.0
     */
    @Value("${pom.version}")
    private String pomVersion;

    /**
     * @since 1.0.0
     */
    @Autowired
    private ApplicationContext appContext;

    /**
     * @since 1.0.0
     */
    @Autowired
    private Executor executor;

    /**
     * Handles the generation of a report based on the provided request. The method validates
     * the client's authorization, processes the report generation, and returns the result
     * within a CompletableFuture wrapped in a ResponseEntity. If the client is unauthorized
     * or an error occurs during report generation, an appropriate error response is returned.
     *
     * @param request The request object containing required parameters for generating the report.
     * @return A CompletableFuture holding a ResponseEntity which wraps the response object.
     * The response indicates the success or failure of the operation, its associated
     * message, and the generated report (if successful).
     * @since 1.0.0
     */
    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/report", method = RequestMethod.POST)
    @ResponseBody
    public CompletableFuture<ResponseEntity<Response>> getReport(@RequestBody RequestReport<?, ?> request) {

        logger.debug("New report request");
        long  startInstant = System.nanoTime();
        IAuth auth         = appContext.getBean(IAuth.class);
        auth.catchRemoteIP();

        return CompletableFuture.supplyAsync(() ->
                {
                    logger.debug("Fetching Response bean from ApplicationContext");
                    Response response = appContext.getBean(Response.class);

                    try {

                        if (auth.isNotAuthorized()) {
                            response.setStatus(Response.Status.ERROR);
                            response.setMessage("Client not authorized");
                            response.setDuration(startInstant, System.nanoTime());
                            logger.debug("Client not authorized, respond with http status code 400");
                            return ResponseEntity.status(400).body(response);
                        }

                        logger.debug("Going to generate the report");

                        Report report = appContext.getBean(Report.class, request);

                        String generatorReport = report.exportAsync().get();

                        if (generatorReport == null) {
                            throw new Exception("Report generation failed, respond with http status code 400");
                        }

                        if (request.getReportType() == RequestReport.ReportType.PRINT) {

                            Printer      printer      = appContext.getBean(Printer.class);
                            PrintService printService = printer.getPrintService(request.getPrinterName());

                            if (request.getAfterPrintOperations().contains(AfterPrintOperations.AFTER_PRINT_CUT_PAPER)) {
                                printer.cutPaper(printService);
                            }

                            if (request.getAfterPrintOperations().contains(AfterPrintOperations.AFTER_PRINT_OPEN_CASH_DRAWER)) {
                                printer.cashDrawer(printService);
                            }

                        } else if (request.getSignPDF() != null) {
                            generatorReport = request.getSignPDF().signPDFAsync(generatorReport, appContext).get();
                        }

                        response.setReport(generatorReport);
                        response.setStatus(Response.Status.OK);
                        response.setDuration(startInstant, System.nanoTime());
                        logger.debug("Report generated elapsed time:{}", response.getDuration());
                        return ResponseEntity.status(200).body(response);
                    } catch (Exception e) {
                        logger.error(e.getMessage());
                        response.setStatus(Response.Status.ERROR);
                        response.setMessage(e.getMessage());
                        response.setDuration(startInstant, System.nanoTime());
                        return ResponseEntity.status(400).body(response);
                    }
                },
                executor
        );
    }

    /**
     * Converts a PDF file into an image representation based on the provided request parameters.
     * This method processes the conversion asynchronously and returns the result encapsulated
     * in a {@link ResponseEntity} with a {@link PdfToImageResponse}.
     *
     * @param request the {@link RequestPdfToImage} object containing the PDF file and optional DPI (Dots Per Inch)
     *                settings for the conversion process
     * @return a {@link CompletableFuture} containing a {@link ResponseEntity} with the {@link PdfToImageResponse},
     *         which includes details about the conversion process, such as the status, images, and duration
     *
     * @since 1.0.0
     */
    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/image", method = RequestMethod.POST)
    @ResponseBody
    public CompletableFuture<ResponseEntity<PdfToImageResponse>> getPdfAsImage(@RequestBody RequestPdfToImage request) {

        logger.debug("New pdf to image request");
        long  startInstant = System.nanoTime();
        IAuth auth         = appContext.getBean(IAuth.class);
        auth.catchRemoteIP();

        return CompletableFuture.supplyAsync(() ->
                {
                    logger.debug("Fetching PDF to image Response bean from ApplicationContext");
                    PdfToImageResponse response = appContext.getBean(PdfToImageResponse.class);
                    try {

                        if (auth.isNotAuthorized()) {
                            response.setStatus(Response.Status.ERROR);
                            response.setMessage("Client not authorized");
                            response.setDuration(startInstant, System.nanoTime());
                            logger.debug("Client not authorized, respond with http status code 400");
                            return ResponseEntity.status(400).body(response);
                        }

                        appContext.getBean(ConvertToImage.class).convert(
                                response, request.getPdf(), request.getDpi()
                        );

                        response.setStatus(Response.Status.OK);
                        response.setDuration(startInstant, System.nanoTime());
                        logger.debug("PDF converted to image elapsed time:{}", response.getDuration());
                        return ResponseEntity.status(200).body(response);
                    } catch (Exception e) {
                        logger.error(e.getMessage());
                        response.setStatus(Response.Status.ERROR);
                        response.setMessage(e.getMessage());
                        response.setDuration(startInstant, System.nanoTime());
                        return ResponseEntity.status(400).body(response);
                    }
                },
                executor
        );
    }

    /**
     * Handles the signing of a document based on the provided request. The method validates
     * the client's authorization, processes the document signing, and returns the result
     * within a CompletableFuture wrapped in a ResponseEntity. If the client is unauthorized
     * or if the input is invalid, an appropriate error response is returned.
     *
     * @param request The request object containing the PDF to be signed and its associated parameters.
     *                It must include a base64-encoded PDF and a signing mechanism.
     * @return A CompletableFuture holding a ResponseEntity which wraps the Response object.
     * The response indicates the success or failure of the operation, its message,
     * and the signed document (if successful).
     * @since 1.0.0
     */
    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/sign", method = RequestMethod.POST)
    @ResponseBody
    public CompletableFuture<ResponseEntity<Response>> signRequest(@RequestBody RequestSign<?> request) {

        logger.debug("New sign request");

        long  startInstant = System.nanoTime();
        IAuth auth         = appContext.getBean(IAuth.class);
        auth.catchRemoteIP();

        return CompletableFuture.supplyAsync(() ->
                {
                    Response response = appContext.getBean(Response.class);

                    try {

                        if (auth.isNotAuthorized()) {
                            response.setStatus(Response.Status.ERROR);
                            response.setMessage("Client not authorized");
                            response.setDuration(startInstant, System.nanoTime());
                            logger.debug("Client not authorized to request sign, respond with http status code 400");
                            return ResponseEntity.status(400).body(response);
                        }

                        if (request.getSignPDF() == null) {
                            response.setStatus(Response.Status.ERROR);
                            response.setMessage("No sign pdf provided");
                            response.setDuration(startInstant, System.nanoTime());
                            logger.debug("No pdf signer provider, respond with http status code 400");
                            return ResponseEntity.status(400).body(response);
                        }

                        if (request.getPdfBase64() == null) {
                            response.setStatus(Response.Status.ERROR);
                            response.setMessage("No pdf base64 provided");
                            response.setDuration(startInstant, System.nanoTime());
                            logger.debug("No pdf base64 provided, respond with http status code 400");
                            return ResponseEntity.status(400).body(response);
                        }

                        logger.debug("Going to sign PDF with {}", request.getSignPDF().getClass().getSimpleName());

                        String signedPdf = request.getSignPDF().signPDFAsync(request.getPdfBase64(), appContext).get();

                        response.setReport(signedPdf);
                        response.setStatus(Response.Status.OK);
                        response.setDuration(startInstant, System.nanoTime());
                        logger.debug("PDF signed elapsed time:{}", response.getDuration());
                        return ResponseEntity.status(200).body(response);
                    } catch (Exception e) {
                        logger.error(e.getMessage());
                        response.setStatus(Response.Status.ERROR);
                        response.setMessage(e.getMessage());
                        response.setDuration(startInstant, System.nanoTime());
                        return ResponseEntity.status(400).body(response);
                    }
                },
                executor
        );
    }

    /**
     * Handles the printing of a document based on the provided request. The method validates
     * the client's authorization, processes the print operation, and returns the result
     * within a CompletableFuture wrapped in a ResponseEntity. If the client is unauthorized,
     * or if the input is invalid, an appropriate error response is returned.
     *
     * @param request The request object containing the print job's details, including the PDF
     *                in base64 format, the printer name, and post-print operations such as
     *                cutting paper or opening the cash drawer.
     * @return A CompletableFuture holding a ResponseEntity which wraps the Response object.
     * The Response indicates the success or failure of the operation, a detailed
     * message, and other related information such as the PDF report (if successful).
     * @since 1.0.0
     */
    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/print", method = RequestMethod.POST)
    @ResponseBody
    public CompletableFuture<ResponseEntity<Response>> signRequest(@RequestBody RequestPrint request) {

        logger.debug("New print request");

        long  startInstant = System.nanoTime();
        IAuth auth         = appContext.getBean(IAuth.class);
        auth.catchRemoteIP();

        return CompletableFuture.supplyAsync(() ->
                {
                    Response response = appContext.getBean(Response.class);

                    try {

                        if (auth.isNotAuthorized()) {
                            response.setStatus(Response.Status.ERROR);
                            response.setMessage("Client not authorized");
                            response.setDuration(startInstant, System.nanoTime());
                            logger.debug("Client not authorized to print request, respond with http status code 400");
                            return ResponseEntity.status(400).body(response);
                        }


                        if (request.getPdfBase64() == null) {
                            response.setStatus(Response.Status.ERROR);
                            response.setMessage("No pdf base64 provided");
                            response.setDuration(startInstant, System.nanoTime());
                            logger.debug("No pdf base64 provided to be printed, respond with http status code 400");
                            return ResponseEntity.status(400).body(response);
                        }

                        logger.debug("Going to pint PDF with");
                        Printer      printer      = appContext.getBean(Printer.class);
                        PrintService printService = printer.getPrintService(request.getPrinterName());

                        printer.print(printService, request.getPdfBase64());

                        if (request.getAfterPrintOperations().contains(AfterPrintOperations.AFTER_PRINT_CUT_PAPER)) {
                            printer.cutPaper(printService);
                        }

                        if (request.getAfterPrintOperations().contains(AfterPrintOperations.AFTER_PRINT_OPEN_CASH_DRAWER)) {
                            printer.cashDrawer(printService);
                        }

                        response.setReport(request.getPdfBase64());
                        response.setStatus(Response.Status.OK);
                        response.setDuration(startInstant, System.nanoTime());
                        logger.debug("PDF print elapsed time:{}", response.getDuration());
                        return ResponseEntity.status(200).body(response);
                    } catch (Exception e) {
                        logger.error(e.getMessage());
                        response.setStatus(Response.Status.ERROR);
                        response.setMessage(e.getMessage());
                        response.setDuration(startInstant, System.nanoTime());
                        return ResponseEntity.status(400).body(response);
                    }
                },
                executor
        );
    }

    /**
     * Handles the operation of cutting the printer paper and opening the cash drawer
     * on a specified printer. This method allows paper feed operations before and
     * after the paper cut as specified by the caller. It also validates the client's
     * authorization before performing the operation. If the operation is unauthorized
     * or an error occurs, an appropriate error response is returned.
     *
     * @param printerName The name of the printer where the operation is to occur. This
     *                    parameter is optional and can be left null to use the default printer.
     * @param feedsBeforeCut The number of paper feed actions to perform before cutting
     *                       the paper. Defaults to 0 if not specified.
     * @param feedsAfterCut The number of paper feed actions to perform after cutting
     *                      the paper. Defaults to 0 if not specified.
     * @return A CompletableFuture containing a ResponseEntity that wraps a Response object.
     *         The Response object includes the status of the operation, a detailed message,
     *         and the duration of the operation.
     *
     * @since 1.0.0
     */
    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/cutandopen", method = RequestMethod.GET)
    @ResponseBody
    public CompletableFuture<ResponseEntity<Response>> cutPaperAndOpenCashDrawer(
            @RequestParam(value = "printerName", required = false) String printerName,
            @RequestParam(value = "feedsBeforeCut", required = false, defaultValue = "0") int feedsBeforeCut,
            @RequestParam(value = "feedsAfterCut", required = false, defaultValue = "0") int feedsAfterCut
    )
    {
        logger.debug("New cut paper and open cash drawer request");

        long  startInstant = System.nanoTime();
        IAuth auth         = appContext.getBean(IAuth.class);
        auth.catchRemoteIP();

        return CompletableFuture.supplyAsync(() -> {

            Response response = appContext.getBean(Response.class);

            try {

                if (auth.isNotAuthorized()) {
                    response.setStatus(Response.Status.ERROR);
                    response.setMessage("Client not authorized");
                    response.setDuration(startInstant, System.nanoTime());
                    logger.debug("Client not authorized to send cut and open cash drawer, respond with http status code 400");
                    return ResponseEntity.status(400).body(response);
                }

                Printer      printer        = appContext.getBean(Printer.class);
                PrintService printerService = printer.getPrintService(printerName);

                if(feedsBeforeCut > 0){
                    for (int i = 0; i < feedsBeforeCut; i++) {
                        printer.feedPaper(printerService);
                    }
                }

                printer.cutAndCashDrawer(printerService);

                if(feedsBeforeCut > 0){
                    for (int i = 0; i < feedsBeforeCut; i++) {
                        printer.feedPaper(printerService);
                    }
                }

                response.setStatus(Response.Status.OK);
                response.setDuration(startInstant, System.nanoTime());
                logger.debug("Request cut and open cash drawer elapsed time:{}", response.getDuration());

                return ResponseEntity.status(200).body(response);
            } catch (Exception e) {
                logger.error(e.getMessage());
                response.setStatus(Response.Status.ERROR);
                response.setMessage(e.getMessage());
                response.setDuration(startInstant, System.nanoTime());
                return ResponseEntity.status(400).body(response);
            }
        }, executor);
    }

    /**
     * Handles the operation of opening the cash drawer on a specified printer.
     * The method validates the client's authorization, performs the operation
     * using the specified printer, and returns the result. If the client is not
     * authorized or an error occurs, an appropriate error response is returned.
     *
     * @param printerName The name of the printer on which the cash drawer operation is
     *                    to be performed. This parameter is optional and can be
     *                    left null to use the default printer.
     * @return A CompletableFuture containing a ResponseEntity that wraps a Response object.
     *         The Response object includes the status of the operation, a message,
     *         and the operation's duration.
     *
     * @since 1.0.0
     */
    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/cashdrawer", method = RequestMethod.GET)
    @ResponseBody
    public CompletableFuture<ResponseEntity<Response>> openCashDrawerPaper(
            @RequestParam(value = "printerName", required = false) String printerName
    ) {

        logger.debug("New open cash drawer request");

        long startInstant = System.nanoTime();
        IAuth auth = appContext.getBean(IAuth.class);
        auth.catchRemoteIP();

        return CompletableFuture.supplyAsync(() -> {

            Response response = appContext.getBean(Response.class);

            try {

                if (auth.isNotAuthorized()) {
                    response.setStatus(Response.Status.ERROR);
                    response.setMessage("Client not authorized");
                    response.setDuration(startInstant, System.nanoTime());
                    logger.debug("Client not authorized to request open cash drawer, respond with http status code 400");
                    return ResponseEntity.status(400).body(response);
                }

                Printer printer = appContext.getBean(Printer.class);
                PrintService printService = printer.getPrintService(printerName);
                printer.cashDrawer(printService);

                response.setStatus(Response.Status.OK);
                response.setDuration(startInstant, System.nanoTime());

                logger.debug("Open cash drawer request elapsed time:{}", response.getDuration());

                return ResponseEntity.status(200).body(response);
            } catch (Exception e) {
                logger.error(e.getMessage());
                response.setStatus(Response.Status.ERROR);
                response.setMessage(e.getMessage());
                response.setDuration(startInstant, System.nanoTime());
                return ResponseEntity.status(400).body(response);
            }
        }, executor);
    }

    /**
     * Handles the operation of cutting the paper on a specified printer.
     * This method allows for additional paper feed actions before and after cutting.
     * It validates the client's authorization and executes the cutting operation
     * if authorized. In case of an error or unauthorized access, an appropriate
     * error is returned.
     *
     * @param printerName The name of the printer where the cut operation is to be
     *                    performed. This parameter is optional and can be left
     *                    null to use the default printer.
     * @param feedsBeforeCut The number of paper feeds to perform before cutting.
     *                       Defaults to 0 if not specified.
     * @param feedsAfterCut The number of paper feeds to perform after cutting.
     *                      Defaults to 0 if not specified.
     * @return A CompletableFuture containing a ResponseEntity that wraps a Response object.
     *         The Response object includes the status of the operation, a message,
     *         and the duration of the operation.
     *
     * @since 1.0.0
     */
    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/cut", method = RequestMethod.GET)
    @ResponseBody
    public CompletableFuture<ResponseEntity<Response>> cutPaper(
            @RequestParam(value = "printerName", required = false) String printerName,
            @RequestParam(value = "feedsBeforeCut", required = false, defaultValue = "0") int feedsBeforeCut,
            @RequestParam(value = "feedsAfterCut", required = false, defaultValue = "0") int feedsAfterCut
    ) {

        logger.debug("New cut paper request");
        long startInstant = System.nanoTime();
        IAuth auth = appContext.getBean(IAuth.class);
        auth.catchRemoteIP();

        return CompletableFuture.supplyAsync(() -> {

            Response response = appContext.getBean(Response.class);

            try {

                if (auth.isNotAuthorized()) {
                    response.setStatus(Response.Status.ERROR);
                    response.setMessage("Client not authorized");
                    response.setDuration(startInstant, System.nanoTime());
                    logger.debug("Client not authorized to request cut paper, respond with http status code 400");
                    return ResponseEntity.status(400).body(response);
                }

                Printer printer = appContext.getBean(Printer.class);
                PrintService printService = printer.getPrintService(printerName);

                if(feedsBeforeCut > 0){
                    for (int i = 0; i < feedsBeforeCut; i++) {
                        printer.feedPaper(printService);
                    }
                }

                printer.cutPaper(printService);

                if (feedsAfterCut > 0) {
                    for (int i = 0; i < feedsAfterCut; i++) {
                        printer.feedPaper(printService);
                    }
                }

                response.setStatus(Response.Status.OK);
                response.setDuration(startInstant, System.nanoTime());
                logger.debug("Request cut elapsed time:{}", response.getDuration());

                return ResponseEntity.status(200).body(response);
            } catch (Exception e) {
                logger.error(e.getMessage());
                response.setStatus(Response.Status.ERROR);
                response.setMessage(e.getMessage());
                response.setDuration(startInstant, System.nanoTime());
                return ResponseEntity.status(400).body(response);
            }
        }, executor);
    }

    /**
     * Handles a GET request to feed paper into a printer. The number of paper feeds
     * and printer name can be specified as parameters.
     *
     * @param printerName the name of the printer to which the paper feed request should be sent;
     *                    if not specified, the default printer is used
     * @param numberOfFeeds the number of times the paper should be fed; defaults to 1 if not provided
     * @return a CompletableFuture containing a ResponseEntity with the response details,
     *         including the status and duration of the operation
     *
     * @since 1.0.0
     */
    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/feedPaper", method = RequestMethod.GET)
    @ResponseBody
    public CompletableFuture<ResponseEntity<Response>> feedPaper(
            @RequestParam(value = "printerName", required = false) String printerName,
            @RequestParam(value = "numberOfFeeds", required = false, defaultValue = "1") int numberOfFeeds
    ) {

        logger.debug("New feeds paper request");
        long startInstant = System.nanoTime();
        IAuth auth = appContext.getBean(IAuth.class);
        auth.catchRemoteIP();

        return CompletableFuture.supplyAsync(() -> {

            Response response = appContext.getBean(Response.class);

            try {

                if (auth.isNotAuthorized()) {
                    response.setStatus(Response.Status.ERROR);
                    response.setMessage("Client not authorized");
                    response.setDuration(startInstant, System.nanoTime());
                    logger.debug("Client not authorized to request paper feed, respond with http status code 400");
                    return ResponseEntity.status(400).body(response);
                }

                Printer printer = appContext.getBean(Printer.class);
                PrintService printService = printer.getPrintService(printerName);

                if(numberOfFeeds > 0){
                    for (int i = 0; i < numberOfFeeds; i++) {
                        printer.feedPaper(printService);
                    }
                }

                response.setStatus(Response.Status.OK);
                response.setDuration(startInstant, System.nanoTime());
                logger.debug("Request feed paper elapsed time:{}", response.getDuration());

                return ResponseEntity.status(200).body(response);
            } catch (Exception e) {
                logger.error(e.getMessage());
                response.setStatus(Response.Status.ERROR);
                response.setMessage(e.getMessage());
                response.setDuration(startInstant, System.nanoTime());
                return ResponseEntity.status(400).body(response);
            }
        }, executor);
    }

    /**
     * Handles a test request to check the communication with the API.
     * @return A CompletableFuture containing a ResponseEntity with the response details,
     *
     * @since 1.0.0
     */
    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    @ResponseBody
    public CompletableFuture<ResponseEntity<Response>> feedPaper() {

        logger.debug("Test communication");
        long startInstant = System.nanoTime();
        IAuth auth = appContext.getBean(IAuth.class);
        auth.catchRemoteIP();

        return CompletableFuture.supplyAsync(() -> {

            Response response = appContext.getBean(Response.class);

            try {

                if (auth.isNotAuthorized()) {
                    response.setStatus(Response.Status.ERROR);
                    response.setMessage("Client not authorized");
                    response.setDuration(startInstant, System.nanoTime());
                    logger.debug("Client not authorized to communicate with API");
                    return ResponseEntity.status(400).body(response);
                }

                response.setStatus(Response.Status.OK);
                response.setMessage("Test communication OK from IP " + auth.getRemoteIp());
                response.setDuration(startInstant, System.nanoTime());
                logger.debug("Request test elapsed time:{}", response.getDuration());

                return ResponseEntity.status(200).body(response);
            } catch (Exception e) {
                logger.error(e.getMessage());
                response.setStatus(Response.Status.ERROR);
                response.setMessage(e.getMessage());
                response.setDuration(startInstant, System.nanoTime());
                return ResponseEntity.status(400).body(response);
            }
        }, executor);
    }
    /**
     * Handles all requests to undefined endpoints in the application. This method captures
     * requests made to paths that do not match any defined controller routes and returns
     * a standardized "Not Found" response.
     *
     * @return A CompletableFuture containing a ResponseEntity that wraps a Response object.
     *         The response indicates an error with a 404 status, a message stating "Action
     *         not found", and no duration information.
     *
     * @since 1.0.0
     */
    @RequestMapping("*")
    public CompletableFuture<ResponseEntity<Response>> catchAllNotFound() {
        return CompletableFuture.supplyAsync(() -> {
            logger.debug("Catch capture a request for a method that not exist");
            Response reportResponse = appContext.getBean(Response.class);
            reportResponse.setStatus(Response.Status.ERROR);
            reportResponse.setMessage("Action not found");
            reportResponse.setDuration(null);
            return ResponseEntity.status(404).body(reportResponse);
        });
    }

}

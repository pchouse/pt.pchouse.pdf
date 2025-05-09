package pt.pchouse.pdf.api.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import pt.pchouse.pdf.api.print.AfterPrintOperations;

import java.util.ArrayList;
import java.util.Objects;

/**
 * The {@code RequestPrint} class is responsible for managing details needed for a printing request,
 * such as the PDF document in Base64 format, the target printer, and post-print operations.
 * <p>
 * This class supports case-insensitive property formats and ensures unknown properties are ignored in
 * JSON serialization/deserialization. It is scoped to a single HTTP request and is a Spring component.
 * <p>
 * The {@code RequestPrint} class also includes logging capabilities to track or debug its actions.
 *
 * @since 1.0.0
 */
@Component
@RequestScope
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class RequestPrint
{

    /**
     * @since 1.0.0
     */
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Represents a PDF document encoded in Base64 format.
     * This field is used to store binary PDF data as a String for easier transmission or processing.
     *
     * @since 1.0.0
     */
    private String pdfBase64;

    /**
     * Represents the name of the printer to which the PDF document will be sent for printing.
     * This field stores a String containing the printer's name or identifier and is
     * used to specify the target printer in the printing process.
     *
     * @since 1.0.0
     */
    private String printerName;

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
     * Default constructor for the RequestSign class.
     * Initializes a new instance of the class and logs the creation.
     *
     * @since 1.0.0
     */
    public RequestPrint() {
        logger.debug("New instance of {}", this.getClass().getName());
    }

    /**
     * Retrieves the Base64-encoded representation of the PDF content.
     *
     * @return a String containing the Base64-encoded PDF data, or null if no data is set.
     *
     * @since 1.0.0
     */
    public String getPdfBase64() {
        return pdfBase64;
    }

    /**
     * Sets the Base64-encoded representation of the PDF content.
     * Logs the action when the PDF content is set.
     *
     * @param pdfBase64 the Base64-encoded string representing the PDF content.
     *
     * @since 1.0.0
     */
    public void setPdfBase64(String pdfBase64) {
        this.pdfBase64 = pdfBase64;
        logger.debug("Report was set");
    }

    /**
     * Retrieves the name of the printer to which the PDF document will be sent for printing.
     *
     * @return a String containing the name or identifier of the target printer.
     */
    public String getPrinterName() {
        return printerName;
    }

    /**
     * Sets the name of the printer to which the PDF document will be sent for printing.
     *
     * @param printerName the name or identifier of the target printer.
     */
    public void setPrinterName(String printerName) {
        this.printerName = printerName;
    }

    /**
     * Retrieves the list of operations to be performed immediately after a print job is completed.
     *
     * @return an ArrayList of {@code AfterPrintOperations}, representing the post-print actions.
     *
     * @since 1.0.0
     */
    public ArrayList<AfterPrintOperations> getAfterPrintOperations() {
        return afterPrintOperations;
    }

    /**
     * Sets the list of operations to be performed immediately after a print job is completed.
     *
     * @param afterPrintOperations an ArrayList of {@code AfterPrintOperations}, representing
     *                             the post-print actions to be executed.
     *
     * @since 1.0.0
     */
    public void setAfterPrintOperations(ArrayList<AfterPrintOperations> afterPrintOperations) {
        this.afterPrintOperations = afterPrintOperations;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RequestPrint that = (RequestPrint) o;
        return Objects.equals(getPdfBase64(), that.getPdfBase64()) &&
                Objects.equals(getPrinterName(), that.getPrinterName()) &&
                Objects.equals(getAfterPrintOperations(), that.getAfterPrintOperations()
                );
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPdfBase64(), getPrinterName(), getAfterPrintOperations());
    }
}

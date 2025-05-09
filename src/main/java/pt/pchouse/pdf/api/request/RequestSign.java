package pt.pchouse.pdf.api.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import pt.pchouse.pdf.api.request.datasource.*;
import pt.pchouse.pdf.api.sign.ISignPDF;
import pt.pchouse.pdf.api.sign.multicert.MulticertSignRequest;

import java.util.ArrayList;
import java.util.Objects;

/**
 * The RequestSign class is a component used to manage PDF signing operations. It is
 * designed to encapsulate the PDF content and the associated signing logic. The class
 * leverages Spring's {@code @Component} and {@code @RequestScope} annotations, making
 * it a request-scoped bean within a Spring application. It is also compatible with JSON
 * serialization and deserialization processes due to the applied Jackson annotations.
 *
 * @param <T> A type parameter that must implement the {@link ISignPDF} interface,
 *            representing the specific implementation of the PDF signing logic. This
 *            ensures flexibility and promotes the ability to integrate various signing
 *            mechanisms through polymorphism.
 */
@Component
@RequestScope
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class RequestSign<T extends ISignPDF>
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
     */
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
    @JsonSubTypes({
            @JsonSubTypes.Type(value = MulticertSignRequest.class, name = "multicert"),
    })
    private ISignPDF signPDF = null;

    /**
     * Default constructor for the RequestSign class.
     * Initializes a new instance of the class and logs the creation.
     *
     * @since 1.0.0
     */
    public RequestSign() {
        logger.debug("New instance of {}", this.getClass().getName());
    }

    /**
     * Constructs a new RequestSign object with the provided signPDF instance.
     * Logs the creation of the object along with the data source information.
     *
     * @param signPDF the instance of type T used for signing PDFs. Can be null.
     *                If null, the log statement indicates a "null" data source.
     *
     * @since 1.0.0
     */
    public RequestSign(T signPDF) {
        logger.debug(
                "New instance of {} with datasource {}",
                this.getClass().getName(),
                signPDF == null ? "null" : signPDF.getClass().getName()
        );
        this.signPDF = signPDF;
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
     *                  It can be null, indicating no PDF content is set.
     *
     * @since 1.0.0
     */
    public void setPdfBase64(String pdfBase64) {
        this.pdfBase64 = pdfBase64;
        logger.debug("Report was set");
    }

    /**
     * Retrieves the ISignPDF instance used for PDF signing operations.
     *
     * @return the ISignPDF instance if it has been set, or null if no instance is assigned.
     *
     * @since 1.0.0
     */
    public ISignPDF getSignPDF() {
        return signPDF;
    }

    /**
     * Sets the ISignPDF instance to be used for signing PDF documents.
     *
     * @param signPDF the ISignPDF instance used for PDF signing operations.
     *
     * @since 1.0.0
     */
    public void setSignPDF(ISignPDF signPDF) {
        this.signPDF = signPDF;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RequestSign<?> that = (RequestSign<?>) o;
        return Objects.equals(getPdfBase64(), that.getPdfBase64()) && Objects.equals(getSignPDF(), that.getSignPDF());
    }

    @Override
    public int hashCode() {
        return Objects.hash(logger, getPdfBase64(), getSignPDF());
    }

}

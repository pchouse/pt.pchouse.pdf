package pt.pchouse.pdf.api.image;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pt.pchouse.pdf.api.Response;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
public class PdfToImageResponse
{
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * The report status response
     *
     * @since 1.0.0
     */

    protected Response.Status status;

    /**
     * Response message
     *
     * @since 1.0.0
     */
    protected String message = "";

    /**
     * The Execution duration human-readable format
     *
     * @since 1.0.0
     */
    protected String duration;

    /**
     * The pdf pages converted to images as base64 encoded
     *
     * @since 1.0.0
     */
    private List<String> images = new ArrayList<>();

    /**
     * The PDF signature
     *
     * @since 1.0.0
     */
    private PdfSignature signature = null;

    /**
     * The request Response
     *
     * @since 1.0.0
     */
    public PdfToImageResponse() {
        logger.debug("New instance of {}", this.getClass().getName());
    }

    /**
     * Get the response status
     *
     * @return The response status
     * @since 1.0.0
     */
    public Response.Status getStatus() {
        return status;
    }

    /**
     * Set the response status
     *
     * @param status The response status
     * @since 1.0.0
     */
    public void setStatus(Response.Status status) {
        this.status = status;
    }

    /**
     * Get the response message
     *
     * @return The response message
     * @since 1.0.0
     */
    public String getMessage() {
        return message;
    }

    /**
     * Set the response message
     *
     * @param message The response message
     * @since 1.0.0
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Get the execution duration
     *
     * @return The execution duration
     * @since 1.0.0
     */
    public String getDuration() {
        return duration;
    }

    /**
     * Set the execution duration
     *
     * @param duration The execution duration
     * @since 1.0.0
     */
    public void setDuration(String duration) {
        this.duration = duration;
    }

    /**
     * Set the human-readable duration time from the start and end nano time
     *
     * @param start The start nano time
     * @param end   The end nano time
     * @since 1.0.0
     */
    public void setDuration(long start, long end) {
        Duration duration = Duration.ofNanos(end - start);
        this.duration = String.format("%ds.%s", duration.getSeconds(), duration.getNano());
    }

    /**
     * Get the pdf pages converted to images as base64 encoded
     *
     * @return The pdf pages converted to images as base64 encoded
     * @since 1.0.0
     */
    public List<String> getImages() {
        return images;
    }

    /**
     * Set the pdf pages converted to images as base64 encoded
     *
     * @param images The pdf pages converted to images as base64 encoded
     * @since 1.0.0
     */
    public void setImages(List<String> images) {
        this.images = images;
    }

    /**
     * Get the pdf signature
     *
     * @return The pdf signature
     * @since 1.0.0
     */
    public PdfSignature getSignature() {
        return signature;
    }

    /**
     * Set the pdf signature
     *
     * @param signature The pdf signature
     * @since 1.0.0
     */
    public void setSignature(PdfSignature signature) {
        this.signature = signature;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PdfToImageResponse that = (PdfToImageResponse) o;
        return getStatus() == that.getStatus() &&
                Objects.equals(getMessage(), that.getMessage()) &&
                Objects.equals(getDuration(), that.getDuration()) &&
                Objects.equals(getImages(), that.getImages()) &&
                Objects.equals(getSignature(), that.getSignature());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStatus(), getMessage(), getDuration(), getImages(), getSignature());
    }
}

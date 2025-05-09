package pt.pchouse.pdf.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Objects;

/**
 * Represents a response object containing status, message, duration, and an optional report.
 * Provides methods to manipulate and retrieve the response details.
 * <p>
 * This class is intended to be used as a prototype bean in a Spring application context.
 */
@Component
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
public class Response
{
    final Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * The report status response
     *
     * @since 1.0.0
     */
    protected Status status;
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
     * The response in base64 encode
     *
     * @since 1.0.0
     */
    private String report = null;

    /**
     * The request Response
     *
     * @since 1.0.0
     */
    public Response() {
        logger.debug("New instance of {}", this.getClass().getName());
    }

    /**
     * Get the generated Report as base64 encoded
     *
     * @return The generated report
     * @since 1.0.0
     */
    public String getReport() {
        return report;
    }

    /**
     * Set the generated report as base64 encoded
     *
     * @param report The generated report
     * @since 1.0.0
     */
    public void setReport(String report) {
        this.report = report;
        logger.debug("Report was set");
    }

    /**
     * Get the response report status
     *
     * @return The status
     * @since 1.0.0
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Set the response report status
     *
     * @param status the status
     * @since 1.0.0
     */
    public void setStatus(Status status) {
        this.status = status;
        logger.debug("Response status set to {}", this.status.toString());
    }

    /**
     * Get the response message
     *
     * @return The message
     * @since 1.0.0
     */
    public String getMessage() {
        return message;
    }

    /**
     * Set the response message
     *
     * @param message The message
     * @since 1.0.0
     */
    public void setMessage(String message) {
        this.message = message;
        logger.debug("Message set to {}", this.message);
    }

    /**
     * The Execution duration human-readable format
     *
     * @return The duration phrase
     * @since 1.0.0
     */
    public String getDuration() {
        return duration;
    }

    /**
     * The Execution duration human-readable format
     *
     * @param duration The duration phrase
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Response that = (Response) o;
        return status == that.status
                && Objects.equals(message, that.message)
                && Objects.equals(report, that.report)
                && Objects.equals(duration, that.duration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, message, report, duration);
    }

    /**
     * The response status enumeration
     *
     * @since 1.0.0
     */
    public enum Status
    {
        OK,
        ERROR,
    }
}

package pt.pchouse.pdf.api.image;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

/**
 * Represents a request object containing a PDF file in base64 format to be converted to images.
 * <p>
 * This class is intended to be used as a request-scoped bean in a Spring application context.
 */
@Component
@RequestScope
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class RequestPdfToImage
{
    /**
     * @since 1.0.0
     */
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * The PDF as base64 string to be converted to images, one image per page.
     *
     * @since 1.0.0
     */
    private String pdf;

    /**
     * The DPI (dots per inch) to be used for the conversion.
     *
     * @since 1.0.0
     */
    private float dpi = 72f;

    public RequestPdfToImage()
    {
        logger.debug("New instance of {}", this.getClass().getName());
    }

    /**
     * Get the DPI (dots per inch) to be used for the conversion.
     *
     * @return The DPI
     * @since 1.0.0
     */
    public float getDpi() {
        return dpi;
    }

    /**
     * Set the DPI (dots per inch) to be used for the conversion.
     *
     * @param dpi The DPI
     * @since 1.0.0
     */
    public void setDpi(float dpi) {
        this.dpi = dpi;
    }

    /**
     * Get the PDF as base64 string to be converted to images, one image per page.
     *
     * @return The PDF as base64 string
     * @since 1.0.0
     */
    public String getPdf() {
        return pdf;
    }

    /**
     * Set the PDF as base64 string to be converted to images, one image per page.
     *
     * @param pdf The PDF as base64 string
     * @since 1.0.0
     */
    public void setPdf(String pdf) {
        this.pdf = pdf;
    }

}

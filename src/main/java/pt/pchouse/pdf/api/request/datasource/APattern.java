package pt.pchouse.pdf.api.request.datasource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract class representing a pattern for data sources.
 * This class provides common handling for date and number patterns for its subclasses.
 * @since 1.0.0
 */
public abstract class APattern
{

    /**
     * @since 1.0.0
     */
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * The Date pattern in the datasource values
     *
     * @since 1.0.0
     */
    private String datePattern;

    /**
     * The number pattern in the datasource values
     *
     * @since 1.0.0
     */
    private String numberPattern;


    /**
     * The Date pattern in the datasource values
     *
     * @return The pattern
     * @since 1.0.0
     */
    public String getDatePattern() {
        return datePattern;
    }

    /**
     * The Date pattern in the datasource values
     *
     * @param datePattern The pattern
     * @since 1.0.0
     */
    public void setDatePattern(String datePattern) {
        this.datePattern = datePattern;
        logger.debug(
                "DatePattern set to {}",
                this.datePattern == null ? "null" : this.datePattern
        );
    }

    /**
     * The number pattern in the datasource values
     *
     * @return The pattern
     * @since 1.0.0
     */
    public String getNumberPattern() {
        return numberPattern;
    }

    /**
     * The number pattern in the datasource values
     *
     * @param numberPattern The pattern
     * @since 1.0.0
     */
    public void setNumberPattern(String numberPattern) {
        this.numberPattern = numberPattern;
        logger.debug(
                "NumberPattern set to {}",
                this.numberPattern == null ? "null" : this.numberPattern
        );
    }
}

package pt.pchouse.pdf.api.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * The PdfProperties class represents a set of properties for configuring a PDF document.
 * This includes user passwords, owner passwords, and embedded JavaScript code.
 * It is designed to be a Spring prototype-scoped component that supports ignoring
 * unknown JSON properties and accepts case-insensitive property names in JSON format.
 * <p>
 * The class provides getters and setters for its fields, with debug logging for updates.
 * It overrides {@link Object#equals(Object)} and {@link Object#hashCode()} for comparison
 * and hashing based on its fields.
 * <p>
 * This class is intended to facilitate PDF-related configurations in a secure and consistent manner.
 *
 * @since 1.0.0
 */
@Component()
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class PdfProperties {

    /**
     *
     * @since 1.0.0
     */
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Pdf user password
     * @since 1.0.0
     */
    private String userPassword;

    /**
     * Pdf owner password
     * @since 1.0.0
     */
    private String ownerPassword;

    /**
     * PDF javascript
     * @since 1.0.0
     */
    private String javascript;

    /**
     * Pdf properties
     * @since 1.0.0
     */
    public PdfProperties() {
        logger.debug("New instance of {}", getClass().getName());
    }

    /**
     * Retrieves the user password associated with the PDF.
     *
     * @return the user password as a string
     */
    public String getUserPassword() {
        return userPassword;
    }

    /**
     * Sets the user password for the PDF.
     *
     * @param userPassword the password to be assigned as the user password
     */
    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
        logger.debug(
                "User password set to '{}'",
                org.apache.commons.lang3.StringUtils.leftPad(
                        "*", this.userPassword.length(), "*"
                )
        );
    }

    /**
     * Get the owner password
     * @return password
     * @since 1.0.0
     */
    public String getOwnerPassword() {
        return ownerPassword;
    }

    /**
     * Set owner password
     * @param ownerPassword password
     * @since 1.0.0
     */
    public void setOwnerPassword(String ownerPassword) {
        this.ownerPassword = ownerPassword;
        logger.debug(
                "Owner password set to '{}'",
                org.apache.commons.lang3.StringUtils.leftPad(
                        "*", this.ownerPassword.length(), "*"
                )
        );
    }

    /**
     * Get the javascript
     * @return javascript code
     * @since 1.0.0
     */
    public String getJavascript() {
        return javascript;
    }

    /**
     * Set the javascript
     * @param javascript javascript code
     * @since 1.0.0
     */
    public void setJavascript(String javascript) {
        this.javascript = javascript;
        logger.debug("Javascript set to: {}", this.javascript);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PdfProperties that = (PdfProperties) o;
        return Objects.equals(userPassword, that.userPassword)
                && Objects.equals(ownerPassword, that.ownerPassword)
                && Objects.equals(javascript, that.javascript);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userPassword, ownerPassword, javascript);
    }

}

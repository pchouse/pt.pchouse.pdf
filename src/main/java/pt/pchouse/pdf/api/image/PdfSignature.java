package pt.pchouse.pdf.api.image;

import java.util.Calendar;

public class PdfSignature
{
    /**
     * The signature name
     *
     * @since 1.0.0
     */
    private String name;

    /**
     * The signature location
     *
     * @since 1.0.0
     */
    private String location;

    /**
     * The signature reason
     *
     * @since 1.0.0
     */
    private String reason;

    /**
     * The signature contact information
     *
     * @since 1.0.0
     */
    private String contactInfo;

    /**
     * The signature date
     *
     * @since 1.0.0
     */
    private Calendar signDate;

    public PdfSignature() {
    }

    /**
     * The signature name
     *
     * @return The signature name
     * @since 1.0.0
     */
    public String getName() {
        return name;
    }

    /**
     * The signature name
     *
     * @param name The signature name
     * @since 1.0.0
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * The signature location
     *
     * @return The signature location
     * @since 1.0.0
     */
    public String getLocation() {
        return location;
    }

    /**
     * The signature location
     *
     * @param location The signature location
     * @since 1.0.0
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * The signature reason
     *
     * @return The signature reason
     * @since 1.0.0
     */
    public String getReason() {
        return reason;
    }

    /**
     * The signature reason
     *
     * @param reason The signature reason
     * @since 1.0.0
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * The signature contact information
     *
     * @return The signature contact information
     * @since 1.0.0
     */
    public String getContactInfo() {
        return contactInfo;
    }

    /**
     * The signature contact information
     *
     * @param contactInfo The signature contact information
     * @since 1.0.0
     */
    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    /**
     * The signature date
     *
     * @return The signature date
     * @since 1.0.0
     */
    public Calendar getSignDate() {
        return signDate;
    }

    /**
     * The signature date
     *
     * @param signDate The signature date
     * @since 1.0.0
     */
    public void setSignDate(Calendar signDate) {
        this.signDate = signDate;
    }
}

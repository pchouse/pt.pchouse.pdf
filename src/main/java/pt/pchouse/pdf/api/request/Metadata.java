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
 * The Metadata class represents metadata associated with a document,
 * including information such as title, author, subject, and various permissions.
 * It provides methods to get and set metadata attributes,
 * as well as to check and control document permissions.
 *
 * @since 1.0.0
 */
@Component()
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class Metadata
{

    /**
     * @since 1.0.0
     */
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Represents the title of the document metadata.
     * This field is used to store the main title or name
     * of the document which can be displayed or modified
     * as required.
     *
     * @since 1.0.0
     */
    private String title;

    /**
     * Represents the author of the document.
     * This property is used to store the name of the individual or entity
     * responsible for the creation of the document.
     *
     * @since 1.0.0
     */
    private String author;

    /**
     * Represents the subject of the document as part of its metadata.
     * This variable holds the subject/topic associated with the document and can
     * be used to provide a summary or description of the document's content.
     *
     * @since 1.0.0
     */
    private String subject;

    /**
     * Represents keywords associated with the document metadata.
     * These keywords can be used for categorization or search purposes.
     *
     * @since 1.0.0
     */
    private String keywords;

    /**
     * Represents the name of the application associated with the document's metadata.
     * This variable stores metadata about the application used in document creation
     * or editing processes.
     *
     * @since 1.0.0
     */
    private String application;

    /**
     * Represents the creator of the document. This field typically identifies
     * the software or entity responsible for generating the document. It may
     * also be used to store metadata regarding the document's origin or
     * authoring tool.
     *
     * @since 1.0.0
     */
    private String creator;

    /**
     * A boolean field representing whether the document's metadata title
     * should be displayed or not.
     * <p>
     * If set to true, the title specified in the metadata will be shown.
     * If false, the title will not be displayed.
     *
     * @since 1.0.0
     */
    private boolean displayMetadataTitle = false;

    /**
     * Represents custom JavaScript associated with the document metadata.
     * This variable can be used to include additional scripting functionality
     * or customization at the metadata level.
     *
     * @since 1.0.0
     */
    private String javascript = null;

    /**
     * Specifies a password to restrict access to the document metadata or its
     * associated features.
     *
     * @since 1.0.0
     */
    private String ownerPassword = null;

    /**
     * Represents the user password associated with the document metadata.
     * This password is used to restrict or allow access to various document operations,
     * depending on the permissions configured.
     *
     * @since 1.0.0
     */
    private String userPassword = null;

    /**
     * Indicates whether the document allows printing.
     *
     * @since 1.0.0
     */
    private boolean allowPrinting = true;

    /**
     * Indicates whether the document allows modifications.
     *
     * @since 1.0.0
     */
    private boolean allowModifying = false;

    /**
     * Indicates whether the document allows copying content.
     *
     * @since 1.0.0
     */
    private boolean allowCopying = false;

    /**
     * Indicates whether the document allows filling in of forms.
     *
     * @since 1.0.0
     */
    private boolean allowFillIn = false;

    /**
     * Indicates whether the document allows screen reader access for accessibility purposes.
     *
     * @since 1.0.0
     */
    private boolean allowScreenReaders = true;

    /**
     * Indicates whether the document allows modifications to its assembly, such as merging or splitting.
     *
     * @since 1.0.0
     */
    private boolean allowAssembly = false;

    /**
     * Indicates whether the document allows degraded printing, such as low-resolution printing.
     *
     * @since 1.0.0
     */
    private boolean allowDegradedPrinting = false;

    /**
     * Indicates whether the document allows modifications to its annotations.
     *
     * @since 1.0.0
     */
    private boolean allowModifyAnnotation = false;

    /**
     * Document metadata
     *
     * @since 1.0.0
     */
    public Metadata() {
        logger.debug("New instance of {}", this.getClass().getName());
    }

    /**
     * Get the document title
     *
     * @return Title
     * @since 1.0.0
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set document title
     *
     * @param title Title
     * @since 1.0.0
     */
    public void setTitle(String title) {
        this.title = title;
        logger.debug("Title set to {}", this.title);
    }

    /**
     * Get document author
     *
     * @return Author
     * @since 1.0.0
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Set the document author
     *
     * @param author Author
     * @since 1.0.0
     */
    public void setAuthor(String author) {
        this.author = author;
        logger.debug("Author set to {}", this.author);
    }

    /**
     * Get the document subject
     *
     * @return Subject
     * @since 1.0.0
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Set the document subject
     *
     * @param subject Subject
     * @since 1.0.0
     */
    public void setSubject(String subject) {
        this.subject = subject;
        logger.debug("Subject set to {}", this.subject);
    }

    /**
     * Get the document keywords
     *
     * @return Keywords
     * @since 1.0.0
     */
    public String getKeywords() {
        return keywords;
    }

    /**
     * Set the document keywords
     *
     * @param keywords Keywords
     * @since 1.0.0
     */
    public void setKeywords(String keywords) {
        this.keywords = keywords;
        logger.debug("Keywords set to {}", this.keywords);
    }

    /**
     * Get application name
     *
     * @return Name
     * @since 1.0.0
     */
    public String getApplication() {
        return application;
    }

    /**
     * Set application name
     *
     * @param application Name
     * @since 1.0.0
     */
    public void setApplication(String application) {
        this.application = application;
        logger.debug("Application set to {}", this.application);
    }

    /**
     * Get document creator
     *
     * @return Creator
     * @since 1.0.0
     */
    public String getCreator() {
        return creator;
    }

    /**
     * Set document creator
     *
     * @param creator Creator
     * @since 1.0.0
     */
    public void setCreator(String creator) {
        this.creator = creator;
        logger.debug("Creator set to {}", this.creator);
    }

    /**
     * Get if the document title is to be displayed
     *
     * @return True if is to be displayed
     * @since 1.0.0
     */
    public boolean isDisplayMetadataTitle() {
        return displayMetadataTitle;
    }

    /**
     * Set if the document is to be displayed
     *
     * @param displayMetadataTitle True if is to be displayed
     * @since 1.0.0
     */
    public void setDisplayMetadataTitle(boolean displayMetadataTitle) {
        this.displayMetadataTitle = displayMetadataTitle;
        logger.debug("Display metadata title set to {}", this.isDisplayMetadataTitle());
    }

    /**
     * Retrieves the JavaScript code associated with the document metadata.
     *
     * @return JavaScript code, or null if none is associated.
     * @since 1.0.0
     */
    public String getJavascript() {
        return javascript;
    }

    /**
     * Assigns custom JavaScript associated with the document metadata for additional
     * scripting functionality.
     *
     * @param javascript JavaScript code to associate.
     * @since 1.0.0
     */
    public void setJavascript(String javascript) {
        this.javascript = javascript;
    }

    /**
     * Retrieves the owner password set for the document metadata.
     *
     * @return Owner password, or null if none is set.
     * @since 1.0.0
     */
    public String getOwnerPassword() {
        return ownerPassword;
    }

    /**
     * Assigns an owner password to restrict access to the document metadata.
     *
     * @param ownerPassword Password to set.
     * @since 1.0.0
     */
    public void setOwnerPassword(String ownerPassword) {
        this.ownerPassword = ownerPassword;
    }

    /**
     * Checks if printing is allowed for the document.
     *
     * @return true if printing is permitted, false otherwise.
     * @since 1.0.0
     */
    public boolean isAllowPrinting() {
        return allowPrinting;
    }

    /**
     * Sets whether printing is allowed for the document.
     *
     * @param allowPrinting true to allow printing, false to restrict it
     * @since 1.0.0
     */
    public void setAllowPrinting(boolean allowPrinting) {
        this.allowPrinting = allowPrinting;
    }

    /**
     * Checks if modifying the document is allowed.
     *
     * @return true if modifying the document is permitted, false otherwise.
     * @since 1.0.0
     */
    public boolean isAllowModifying() {
        return allowModifying;
    }

    /**
     * Sets whether modifying the document is allowed or restricted.
     *
     * @param allowModifying true to allow modifications to the document, false to restrict modifications.
     * @since 1.0.0
     */
    public void setAllowModifying(boolean allowModifying) {
        this.allowModifying = allowModifying;
    }

    /**
     * Determines whether copying content from the document is allowed.
     *
     * @return true if copying is permitted, false otherwise.
     *
     * @since 1.0.0
     */
    public boolean isAllowCopying() {
        return allowCopying;
    }

    /**
     * Sets whether copying content from the document is allowed or restricted.
     *
     * @param allowCopying true to allow copying content from the document, false to restrict it
     *
     * @since 1.0.0
     */
    public void setAllowCopying(boolean allowCopying) {
        this.allowCopying = allowCopying;
    }

    /**
     * Checks if filling in forms or annotations is allowed for the document.
     *
     * @return true if filling in forms or annotations is permitted, false otherwise.
     *
     * @since 1.0.0
     */
    public boolean isAllowFillIn() {
        return allowFillIn;
    }

    /**
     * Sets whether filling in forms or annotations is allowed for the document.
     *
     * @param allowFillIn true to allow filling in forms or annotations, false to restrict it
     * @since 1.0.0
     */
    public void setAllowFillIn(boolean allowFillIn) {
        this.allowFillIn = allowFillIn;
    }

    /**
     * Checks if screen readers are allowed for the document.
     *
     * @return true if screen readers are permitted, false otherwise.
     * @since 1.0.0
     */
    public boolean isAllowScreenReaders() {
        return allowScreenReaders;
    }

    /**
     * Sets whether screen readers are allowed to access the document.
     *
     * @param allowScreenReaders true to allow screen readers access, false to restrict it
     * @since 1.0.0
     */
    public void setAllowScreenReaders(boolean allowScreenReaders) {
        this.allowScreenReaders = allowScreenReaders;
    }

    /**
     * Checks if document assembly is allowed.
     *
     * @return true if assembly is permitted, false otherwise.
     * @since 1.0.0
     */
    public boolean isAllowAssembly() {
        return allowAssembly;
    }

    /**
     * Sets whether document assembly is allowed.
     *
     * @param allowAssembly true to allow document assembly, false to restrict it
     * @since 1.0.0
     */
    public void setAllowAssembly(boolean allowAssembly) {
        this.allowAssembly = allowAssembly;
    }

    /**
     * Checks if degraded printing of the document is allowed.
     *
     * @return true if degraded printing is permitted, false otherwise.
     * @since 1.0.0
     */
    public boolean isAllowDegradedPrinting() {
        return allowDegradedPrinting;
    }

    /**
     * Sets whether degraded printing of the document is allowed. Degraded printing typically
     * allows users to print the document at a lower quality, even when full-quality printing
     * is restricted.
     *
     * @param allowDegradedPrinting true to allow degraded printing, false to restrict it
     * @since 1.0.0
     */
    public void setAllowDegradedPrinting(boolean allowDegradedPrinting) {
        this.allowDegradedPrinting = allowDegradedPrinting;
    }

    /**
     * Determines whether modifying annotations in the document is allowed.
     *
     * @return true if modifying annotations is permitted, false otherwise.
     * @since 1.0.0
     */
    public boolean isAllowModifyAnnotation() {
        return allowModifyAnnotation;
    }

    /**
     * Sets whether modifying annotations in the document is allowed or restricted.
     *
     * @param allowModifyAnnotation true to allow modifying annotations, false to restrict it
     * @since 1.0.0
     */
    public void setAllowModifyAnnotation(boolean allowModifyAnnotation) {
        this.allowModifyAnnotation = allowModifyAnnotation;
    }

    /**
     * Retrieves the user password set for the document metadata.
     *
     * @return User password, or null if none is set.
     *
     * @since 1.0.0
     */
    public String getUserPassword() {
        return userPassword;
    }

    /**
     * Assigns a user password to restrict access to the document metadata.
     *
     * @param userPassword Password to set for the user.
     *
     * @since 1.0.0
     */
    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Metadata metadata = (Metadata) o;
        return isDisplayMetadataTitle() == metadata.isDisplayMetadataTitle() && isAllowPrinting() == metadata.isAllowPrinting() && isAllowModifying() == metadata.isAllowModifying() && isAllowCopying() == metadata.isAllowCopying() && isAllowFillIn() == metadata.isAllowFillIn() && isAllowScreenReaders() == metadata.isAllowScreenReaders() && isAllowAssembly() == metadata.isAllowAssembly() && isAllowDegradedPrinting() == metadata.isAllowDegradedPrinting() && Objects.equals(logger, metadata.logger) && Objects.equals(getTitle(), metadata.getTitle()) && Objects.equals(getAuthor(), metadata.getAuthor()) && Objects.equals(getSubject(), metadata.getSubject()) && Objects.equals(getKeywords(), metadata.getKeywords()) && Objects.equals(getApplication(), metadata.getApplication()) && Objects.equals(getCreator(), metadata.getCreator()) && Objects.equals(getJavascript(), metadata.getJavascript()) && Objects.equals(getOwnerPassword(), metadata.getOwnerPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                logger,
                getTitle(),
                getAuthor(),
                getSubject(),
                getKeywords(),
                getApplication(),
                getCreator(),
                isDisplayMetadataTitle(),
                getJavascript(),
                getOwnerPassword(),
                isAllowPrinting(),
                isAllowModifying(),
                isAllowCopying(),
                isAllowFillIn(),
                isAllowScreenReaders(),
                isAllowAssembly(),
                isAllowDegradedPrinting()
        );
    }
}

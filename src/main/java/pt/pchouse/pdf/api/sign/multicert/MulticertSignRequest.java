package pt.pchouse.pdf.api.sign.multicert;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pt.pchouse.pdf.api.sign.ISignPDF;
import pt.pchouse.pdf.api.sign.multicert.model.Base64Document;
import pt.pchouse.pdf.api.sign.multicert.model.SignDocumentBase64Document;
import pt.pchouse.pdf.api.sign.multicert.model.SignRequestBase64Document;
import pt.pchouse.pdf.api.sign.multicert.model.SignatureInfo;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

/**
 * The MulticertSignRequest class is responsible for handling PDF signing requests.
 * It provides functionality to sign a PDF document using base64-encoded content
 * and interacts with external systems for signing through the specified credentials and configurations.
 * This class allows setting various properties required for the signing process, such as
 * user credentials, external references, document details, and signature information.
 * <p>
 * This class implements the ISignPDF interface, which defines the contract for signing PDF files.
 * It uses the provided user credentials and integrates with the external signing API to carry out
 * the signing operation. The behavior can be configured for either test or production environments.
 * <p>
 * This component is designed with prototype scope, allowing each usage to have an independent instance.
 *
 * @since 1.0.0
 */
@Component
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class MulticertSignRequest implements ISignPDF
{
    /**
     * Represents the main signing component used for handling the PDF signing process.
     * This dependency is automatically injected into the class.
     *
     * @since 1.0.0
     */
    @Autowired
    Sign sign;

    /**
     * Represents the username associated with the sign request.
     * This field is used to identify the user initiating the request.
     *
     * @since 1.0.0
     */
    private String username;

    /**
     * Represents the password associated with the signing request.
     * This is used for authentication or security purposes.
     * It must be securely managed to prevent unauthorized access.
     *
     * @since 1.0.0
     */
    private String password;

    /**
     * Represents an external reference identifier related to the signing request.
     * This value can be used to link the request to an external system or process.
     *
     * @since 1.0.0
     */
    private String externalReference;

    /**
     * Represents a brief description or details regarding the specific
     * signature request. This field can be used to provide context or
     * additional information about what the signing process entails.
     *
     * @since 1.0.0
     */
    private String description;

    /**
     * Represents the name of the file associated with the sign request.
     * This field holds the file's name that is either to be signed or associated
     * with the signing process.
     *
     * @since 1.0.0
     */
    private String fileName;

    /**
     * Represents the signature information associated with the sign request.
     * Contains metadata and configurations required for applying a signature
     * to a document, such as signature type, visible signature details,
     * reason, location, timestamp settings, and advanced signature configurations.
     *
     * @since 1.0.0
     */
    private SignatureInfo signatureInfo;

    /**
     * Indicates whether the current operation is executed in test mode.
     * When set to true, the operation does not perform actual signing.
     *
     * @since 1.0.0
     */
    private boolean isTest;

    /**
     * Asynchronously signs a PDF document provided in base64 format
     * and returns the signed content in base64 format.
     *
     * @param pdfBase64 A string containing the content of the PDF document in base64 format.
     * @param appContext The Spring  application context.
     * @return A CompletableFuture that resolves to the signed PDF document as a base64 string.
     * If an error occurs during the signing process, the CompletableFuture completes exceptionally.
     * @since 1.0.0
     */
    public CompletableFuture<String> signPDFAsync(String pdfBase64, ApplicationContext appContext) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if(appContext != null && sign == null){
                    sign = appContext.getBean(Sign.class);
                }
                return signPDF(pdfBase64, appContext);
            } catch (Exception e) {
                throw new CompletionException(e);
            }
        });
    }

    /**
     * Signs the PDF document provided in base64 format and returns the base64 signed content.
     *
     * @param pdfBase64 A string containing the content of the PDF document in base64 format.
     * @return The signed PDF document as a base64 string.
     * @throws Exception If an error occurs during the signing process.
     * @since 1.0.0
     */
    @Override
    public String signPDF(String pdfBase64, ApplicationContext appContext) throws Exception {

        Base64Document base64Document = new Base64Document();
        base64Document.setExternalReference(externalReference);
        base64Document.setName(fileName);
        base64Document.setContentType(Base64Document.ContentTypeEnum.APPLICATION_PDF);
        base64Document.setBase64Content(pdfBase64);

        SignDocumentBase64Document signDocumentBase64Document = new SignDocumentBase64Document();
        signDocumentBase64Document.documentRequest(base64Document);
        signDocumentBase64Document.signatureInfo(signatureInfo);

        SignRequestBase64Document signRequestBase64Document = new SignRequestBase64Document();
        signRequestBase64Document.setExternalReference(externalReference + "_Request");
        signRequestBase64Document.setDescription(description == null ? "" : description);
        signRequestBase64Document.addDocumentsItem(signDocumentBase64Document);
        signRequestBase64Document.setReturnSignedContent(true);

        if(appContext != null && sign == null){
            sign = appContext.getBean(Sign.class);
        }

        return sign.signBase64(username, password, signRequestBase64Document, isTest);
    }

    /**
     * Retrieves the username associated with the request.
     *
     * @return The username as a string.
     * @since 1.0.0
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username associated with the request.
     *
     * @param username The username to be set as a string.
     * @since 1.0.0
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Retrieves the password associated with the request.
     *
     * @return The password as a string.
     * @since 1.0.0
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password associated with the request.
     *
     * @param password The password to be set as a string.
     * @since 1.0.0
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Retrieves the external reference associated with the request.
     *
     * @return The external reference as a string.
     * @since 1.0.0
     */
    public String getExternalReference() {
        return externalReference;
    }

    /**
     * Sets the external reference associated with the request.
     *
     * @param externalReference The external reference to be set as a string.
     * @since 1.0.0
     */
    public void setExternalReference(String externalReference) {
        this.externalReference = externalReference;
    }

    /**
     * Retrieves the description associated with the request.
     *
     * @return The description as a string.
     * @since 1.0.0
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description associated with the request.
     *
     * @param description The description to be set as a string.
     * @since 1.0.0
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Retrieves the file name associated with the request.
     *
     * @return The file name as a string.
     * @since 1.0.0
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Sets the file name associated with the request.
     *
     * @param fileName The file name to be set as a string.
     * @since 1.0.0
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Retrieves the signature information associated with the request.
     *
     * @return The signature information as a SignatureInfo object.
     * @since 1.0.0
     */
    public SignatureInfo getSignatureInfo() {
        return signatureInfo;
    }

    /**
     * Sets the signature information associated with the request.
     *
     * @param signatureInfo The signature information to be set as a SignatureInfo object.
     * @since 1.0.0
     */
    public void setSignatureInfo(SignatureInfo signatureInfo) {
        this.signatureInfo = signatureInfo;
    }

    /**
     * Checks whether the request is marked as a test.
     *
     * @return true if the request is a test, false otherwise.
     * @since 1.0.0
     */
    public boolean isTest() {
        return isTest;
    }

    /**
     * Checks whether the request is marked as a test.
     *
     * @return true if the request is a test, false otherwise.
     * @since 1.0.0
     */
    public boolean getIsTest() {
        return isTest;
    }

    /**
     * Sets the test flag for the request.
     *
     * @param test A boolean value indicating whether the request is a test.
     * @since 1.0.0
     */
    public void setTest(boolean test) {
        isTest = test;
    }

    /**
     * Sets the test flag for the request.
     *
     * @param test A boolean value indicating whether the request is a test.
     * @since 1.0.0
     */
    public void setIsTest(boolean test) {
        isTest = test;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        MulticertSignRequest that = (MulticertSignRequest) o;
        return isTest() == that.isTest() &&
                Objects.equals(getUsername(), that.getUsername()) &&
                Objects.equals(getPassword(), that.getPassword()) &&
                Objects.equals(getExternalReference(), that.getExternalReference()) &&
                Objects.equals(getDescription(), that.getDescription()) &&
                Objects.equals(getFileName(), that.getFileName()) &&
                Objects.equals(getSignatureInfo(), that.getSignatureInfo());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                getUsername(),
                getPassword(),
                getExternalReference(),
                getDescription(),
                getFileName(),
                getSignatureInfo(),
                isTest()
        );
    }
}

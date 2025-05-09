package pt.pchouse.pdf.api.sign.multicert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pt.pchouse.pdf.api.sign.multicert.model.*;
import pt.pchouse.pdf.api.sign.multicert.service.SignStashDocumentSignService;
import retrofit2.Call;
import retrofit2.Response;

import java.time.Instant;
import java.util.HashMap;

/**
 * The {@code Sign} class provides methods to handle authentication (`jwt`) and
 * document signing operations (`signBase64`) with the SignStash API.
 * This class relies on dependencies for API client creation and API authentication.
 * It is a spring-managed singleton component designed to encapsulate SignStash
 * operations efficiently.
 * <p>
 * Key functionalities:
 * - Authenticates with the SignStash API using username, password, and test mode.
 * - Manages JWT tokens for efficient reusability and reduces redundant authentication requests.
 * - Signs a base64-encoded document with support for various error handling scenarios.
 * <p>
 * This class ensures detailed logging for debugging and operational tracking.
 *
 * @since 1.0.0
 */
@Component()
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class Sign
{
    /**
     * @since 1.0.0
     */
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * A singleton scoped bean that provides authentication operations for the
     * SignStash API integration. This field is automatically injected by Spring's
     * Dependency Injection framework using the @Autowired annotation. The class
     * SignStashApiAuthentication is responsible for creating authentication
     * services and providing the appropriate base URL for the API, based on
     * whether the environment is for testing or production.
     * <p>
     * This field is used internally within the Sign class for performing actions
     * like generating JWT tokens and signing documents via the SignStash API.
     *
     * @since 1.0.0
     */
    @Autowired
    private SignStashApiAuthentication signStashApiAuthentication;

    /**
     * A client for interacting with the SignStash API, used for handling API communication
     * related operations such as creating services or retrieving base URLs.
     * This is a singleton bean managed by Spring and is injected into the containing class.
     *
     * @since 1.0.0
     */
    @Autowired
    private SignStashApiClient apiClient;

    /**
     * Default constructor for the Sign class.
     * <p>
     * This constructor initializes a new instance of the Sign class and logs
     * a message indicating that the instance has been initialized successfully.
     * Primary responsibility is to prepare the Sign object for use by setting
     * up required internal components or dependencies.
     *
     * @since 1.0.0
     */
    public Sign() {
        logger.info("Sign initialized");
    }

    /**
     * A map that holds JWT (JSON Web Token) authentication responses, keyed by a unique identifier string.
     * <p>
     * This data structure is used to store and retrieve authentication-related information where the key represents
     * an identifier (e.g., a username, session ID, etc.), and the value contains the associated authentication
     * response, encapsulated in an {@link AuthenticationResponse} object.
     * <p>
     * The map is final, ensuring that the reference cannot be reassigned, providing integrity and consistency
     * within the context of usage in the {@code Sign} class.
     *
     * @since 1.0.0
     */
    private final HashMap<String, AuthenticationResponse> jwtList = new HashMap<>();

    /**
     * Generates a JWT (JSON Web Token) for the given username and password.
     * This method authenticates the user with the provided credentials and returns an access token.
     * The token is cached and reused if it is not expired.
     *
     * @param username The username for the authentication request.
     * @param password The password for the authentication request.
     * @param isTest   A flag indicating whether the authentication request is for a test environment.
     * @return A valid access token as a string.
     * @throws Exception If there is an error during the authentication process,
     *                   or if the response contains a null access token.
     * @since 1.0.0
     */
    public String jwt(String username, String password, boolean isTest) throws Exception {

        logger.debug("Checking for cached JWT token for username: {}", username);
        if (jwtList.containsKey(username)) {

            AuthenticationResponse authResponse = jwtList.get(username);

            if (authResponse.getExpires().minusSeconds(60 * 5).isAfter(Instant.now())) {
                return authResponse.getAccessToken();
            } else {
                logger.warn("JWT token for username: {} is expired and being removed", username);
                jwtList.remove(username);
            }
        }

        Response<AuthenticationResponse> response = signStashApiAuthentication
                .createService(username, password, isTest)
                .authenticate("client_credentials")
                .execute();

        if (response.isSuccessful()) {
            AuthenticationResponse authResponse = response.body();
            if (authResponse == null || authResponse.getAccessToken() == null) {
                String msg = "";
                if (response.errorBody() != null) {
                    msg = String.format("SignStash authentication request failed: %s", response.errorBody().string());
                    logger.error(msg);
                }
                logger.error("Authentication response returned null or invalid access token for username: {}", username);
                throw new Exception(
                        msg.isEmpty() ? "Authentication return access token null" : msg
                );
            }
            jwtList.put(username, authResponse);
            logger.info("New JWT token retrieved successfully for username: {}", username);
            return authResponse.getAccessToken();
        } else {
            logger.error("SignStash authentication request failed for username: {}", username);
            if (response.errorBody() != null) {
                var error = response.errorBody().string();
                System.out.println(error);
                logger.error("Error body: {}", error);
            }
            throw new Exception("SigStash authentication request failed");
        }
    }

    /**
     * Performs a signing operation using the SignStash API.
     *
     * @param username    SignStash username
     * @param password    SignStash password
     * @param signRequest The request object containing details of the document to be signed
     * @param isTest      A flag indicating whether the operation should be performed in a test environment
     * @return A Response object containing the result of the signing operation
     * @throws Exception If the signing operation fails or if there is an error during the process
     */
    protected Response<SignOperation> doCall(
            String username, String password, SignRequestBase64Document signRequest, boolean isTest
    ) throws Exception
    {
        SignStashDocumentSignService api = apiClient.createService(
                SignStashDocumentSignService.class,
                jwt(username, password, isTest),
                isTest
        );

        logger.debug("API client created successfully for username: {}", username);

        Call<SignOperation> call = api.signDocumentBatchBase64(signRequest);

        return call.execute();
    }

    /**
     * Reshapes the SignRequest object to ensure that it meets the requirements
     *
     * @param signRequest The SignRequest object to be reshaped
     */
    protected void reshapeSignRequest(SignRequestBase64Document signRequest) {
        for (var document : signRequest.getDocuments()) {
            if (document.getSignatureInfo() != null) {
                // 2.1.1 Is documents[].signatureInfo.advancedSignatureInfo structure being ignored when requesting PDF signatures ?
                document.getSignatureInfo().setAdvancedSignatureInfo(null);
                // 2.1.2	Is documents[].signatureInfo.applyTimestamp=”false” for invoice signing that doesn’t require archive (premium only feature) ?
                document.getSignatureInfo().setApplyTimestamp(false);
            }
        }
        signRequest.setReturnSignedContent(true);
    }

    /**
     * Signs a document in Base64 format using the provided credentials and request parameters.
     * This method interacts with the SignStash API to perform the signing operation and returns
     * the signed content if successful.
     *
     * @param username    The username for authentication.
     * @param password    The password for authentication.
     * @param signRequest The request object containing details of the document to be signed in Base64 format.
     * @param isTest      A flag indicating whether the operation should be performed in a test environment.
     * @return A string containing the Base64 representation of the signed content.
     * @throws Exception If the signing operation fails or if there is an error during the process.
     * @since 1.0.0
     */
    public String signBase64(String username, String password, SignRequestBase64Document signRequest, boolean isTest)
            throws Exception
    {
        logger.debug("signBase64 method called with username: {}, isTest: {}, and signRequest: {}", username, isTest, signRequest);

        reshapeSignRequest(signRequest);

        var response = doCall(username, password, signRequest, isTest);

        if (response.code() == 401) {
            logger.error("SignStash Authorization Bearer error 401 going to retrieves for user {}", username);
            jwtList.remove(username);
            response = doCall(username, password, signRequest, isTest);
        }

//        The type of application where is to be used doesn't make sense to put the user waiting for 3 minutes
//        if fails to sign the document the error is shown immediately
//
//        if(response.code() != 200) {
//
//            logger.error("SignStash signDocumentBatchBase64 request failed with code: {}", response.code());
//
//            for(int retry = 1; retry <= 3; retry++) {
//                Thread.sleep(1000 * 60);
//                response = doCall(username, password, signRequest, isTest);
//                if(response.code() == 200)  break;
//                logger.error(
//                        "SignStash signDocumentBatchBase64 request failed with code: {}, retry: {}",
//                        response.code(),
//                        retry
//                );
//            }
//        }

        SignOperation signOperation = response.body();

        logger.debug("API call executed for signing document batch, response received");

        if (signOperation == null) {
            var errorBody = response.errorBody();

            if (errorBody != null) {

                var error = ResponseError.Parse(errorBody.string());

                if (error != null) {
                    var msg = String.format(
                            "Error signing document: %s, %s, %s",
                            error.getErrorCode(),
                            error.getErrorCodeDescription(),
                            error.getCauseMessage()
                    );
                    logger.error(msg);
                    throw new Exception(msg);
                }
            }

            logger.error("SignStash signDocumentBatchBase64 request failed: response is null");
            throw new Exception("SignStash signDocumentBatchBase64 request failed");
        }

        if (signOperation.getDocumentList().isEmpty()) {
            logger.error("SignStash signDocumentBatchBase64 request failed: returned document list is empty");
            throw new Exception("Sign Stash request failed: returned document list is empty");
        }

        DocumentResponse documentResponse = signOperation.getDocumentList().getFirst();

        if (documentResponse.getSignStatus() != SigningStatus.SIGNED) {
            String error = switch (documentResponse.getSignError()) {
                case SigningError.CONFIGURATION_SIGN_ERROR -> "SignStash request failed with configuration error";
                case SigningError.CERT_SERVICE_GLOBAL_ERROR -> "SignStash request failed with global error";
                case SigningError.TIMESTAMP_SIGN_ERROR -> "SignStash request failed with timestamp error";
                case SigningError.INVALID_CERT_SERVICE_CREDS -> String.format("Conta Multicert ID '%s' sem créditos", username);
                case SigningError.INVALID_CERTIFICATE_SIGN_ERROR -> "SignStash request failed with invalid certificate";
                case SigningError.SIGN_OPERATION_INTERNAL_ERROR -> "SignStash request failed with internal error";
                case SigningError.INVALID_CERT_ACCESS_ACCOUNT -> String.format("Acesso à conta Multicert ID '%s' inválido", username);
                case SigningError.STORAGE_SERVICE_GLOBAL_ERROR -> "SignStash request failed with global storage error";
                default -> "SignStash request failed with unknown error";
            };

            logger.error(error);
            throw new Exception(error);
        }

        logger.debug("Document signed successfully for username: {}", username);

        return documentResponse.getSignedContent();
    }

}

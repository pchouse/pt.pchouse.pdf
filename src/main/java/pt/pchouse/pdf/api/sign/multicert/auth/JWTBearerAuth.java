package pt.pchouse.pdf.api.sign.multicert.auth;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The JWTBearerAuth class is an implementation of the Interceptor interface
 * that facilitates authentication using JSON Web Tokens (JWT) with the `Bearer`
 * token scheme in HTTP requests. It adds the `Authorization` header with the
 * provided API key to outgoing requests.
 * <p>
 * The class allows an API key to be set either during the creation of the instance
 * or later through the setter method. The provided API key is used to construct
 * the Bearer token, which is then attached to the HTTP request.
 * <p>
 * The intercept method ensures that the API key is included in every request
 * made through the OkHttp client. If the API key is not set, an exception is thrown.
 */
public class JWTBearerAuth implements Interceptor
{
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * The API key used for authentication in HTTP requests.
     * This field stores the API key that is appended to the `Authorization` header
     * with the `Bearer` scheme. It is a required parameter for authenticating
     * outgoing requests. If not set, an exception is thrown during request interception.
     *
     * @since 1.0.0
     */
    private String apiKey;

    /**
     * Constructs a new instance of the JWTBearerAuth class.
     * <p>
     * This default constructor initializes the object without setting an API key.
     * The API key can be set later using the provided setter method. This approach
     * allows flexibility in scenarios where the API key is not available at the time
     * of instance creation.
     *
     * @since 1.0.0
     */
    public JWTBearerAuth() {
        logger.debug("JWTBearerAuth created");
    }

    /**
     * Constructs an instance of the JWTBearerAuth class with the specified API key.
     * This constructor initializes the `apiKey` field, which is used to generate
     * the `Authorization` header with the `Bearer` token scheme. The API key provides
     * the required credentials for authenticating HTTP requests.
     *
     * @param apiKey the API key to be used for authentication in HTTP requests
     *               
     * @since 1.0.0
     */
    public JWTBearerAuth(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * Retrieves the API key used for authentication in HTTP requests.
     *
     * @return the API key stored in this instance
     *
     * @since 1.0.0
     */
    public String getApiKey() {
        return apiKey;
    }

    /**
     * Sets the API key to be used for authentication in HTTP requests.
     * This method updates the `apiKey` field in this instance, which is
     * used to generate the `Authorization` header with the `Bearer` token scheme.
     *
     * @param apiKey the API key to be used for authentication
     *
     * @since 1.0.0
     */
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * Intercepts HTTP requests to add an `Authorization` header using a Bearer token for authentication.
     *
     * @param chain the {@link Chain} object containing the request and providing a way to proceed or stop
     *              the execution of the HTTP call.
     * @return a {@link Response} object representing the server's response to the authenticated HTTP request.
     * @throws IOException if an I/O error occurs during request execution.
     * @throws IllegalArgumentException if the required `apiKey` is not set in the instance.
     *
     * @since 1.0.0
     */
    @Override
    public Response intercept(Chain chain) throws IOException {
        logger.debug("Intercepting HTTP request...");

        if (apiKey == null) {
            throw new IllegalArgumentException(
                    "Missing the required parameter 'apiKey' when calling JWTBearerAuth"
            );
        }

        logger.debug("Setting Authorization header with Bearer token.");

        Request request = chain.request();

        request = request.newBuilder()
                  .addHeader("Authorization", "Bearer " + apiKey)
                  .build();

        logger.debug("Proceeding with the HTTP request.");
        return chain.proceed(request);
    }
}

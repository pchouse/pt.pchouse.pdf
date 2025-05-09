package pt.pchouse.pdf.api.sign.multicert.auth;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Credentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The HttpBasicAuth class is an implementation of the Interceptor interface 
 * that allows for Basic HTTP Authentication with OkHttp requests. It adds 
 * the required `Authorization` header to the outgoing HTTP requests.
 * <p>
 * Credentials can be provided during the creation of the instance or set 
 * later via setter methods. This class encodes the provided username and password 
 * in Base64 format as per the Basic Authentication specification.
 * <p>
 * The intercept method ensures that the credentials are attached to each HTTP 
 * request made through the OkHttp client.
 * 
 * @since 1.0.0
 */
public class HttpBasicAuth implements Interceptor
{

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * The username to be used for Basic HTTP Authentication.
     * This field stores the username credential which is encoded
     * along with the password to generate the `Authorization` header
     * required for authenticating HTTP requests. It can be set directly
     * or through methods such as {@link #setCredentials(String, String)}.
     * 
     * @since 1.0.0
     */
    private String username;

    /**
     * The password to be used for Basic HTTP Authentication.
     * This field stores the password credential which is encoded
     * along with the username to generate the `Authorization` header
     * required for authenticating HTTP requests. It can be set directly
     * or through methods such as {@link #setCredentials(String, String)}.
     */
    private String password;

    /**
     * Constructs a new instance of the HttpBasicAuth class.
     * <p>
     * This default constructor initializes the object without setting any credentials.
     * It is useful when credentials will be provided later through the setter methods.
     * A log entry is created at the debug level indicating the creation of the instance.
     *
     * @since 1.0.0
     */
    public HttpBasicAuth() {
        logger.debug("HttpBasicAuth created");
    }

    /**
     * Constructs an instance of the HttpBasicAuth class with the specified username and password.
     * <p>
     * This constructor initializes the credentials by calling {@link #setCredentials(String, String)},
     * which sets the provided username and password. These credentials will be used to generate 
     * the `Authorization` header for HTTP Basic Authentication.
     *
     * @param username the username to be used for Basic HTTP Authentication
     * @param password the password to be used for Basic HTTP Authentication
     *
     * @since 1.0.0
     */
    public HttpBasicAuth(String username, String password) {
        this.setCredentials(username, password);
    }

    /**
     * Retrieves the username used for Basic HTTP Authentication.
     *
     * @return the username credential stored in this instance
     *
     * @since 1.0.0
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username to be used for Basic HTTP Authentication.
     * This method updates the username field for the instance, which will be
     * used in combination with the password to generate the `Authorization` header
     * for HTTP requests.
     *
     * @param username the username to be stored for authentication
     *
     * @since 1.0.0
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Retrieves the password used for Basic HTTP Authentication.
     *
     * @return the password credential stored in this instance
     *
     * @since 1.0.0
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password to be used for Basic HTTP Authentication.
     * This method updates the password field for the instance, 
     * which will be used in combination with the username to 
     * generate the `Authorization` header for HTTP requests.
     *
     * @param password the password to be stored for authentication
     *
     * @since 1.0.0
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Sets the credentials for Basic HTTP Authentication by updating the username 
     * and password fields of this instance. These credentials are used to generate 
     * the `Authorization` header for HTTP requests.
     *
     * @param username the username to be stored for authentication
     * @param password the password to be stored for authentication
     *
     * @since 1.0.0
     */
    public void setCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Intercepts HTTP requests and adds an `Authorization` header for Basic HTTP Authentication 
     * using the configured username and password credentials.
     *
     * @param chain the {@link Chain} object containing the request and 
     *              providing a way to proceed or stop the execution of the HTTP call.
     * @return a {@link Response} object representing the server's response to the 
     *         authenticated HTTP request.
     * @throws IOException if an I/O error occurs during request execution.
     *
     * @since 1.0.0
     */
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        logger.debug("Attempting to add Authorization header with Basic Auth credentials");

        String credentials = Credentials.basic(username, password, StandardCharsets.UTF_8);
        request = request.newBuilder()
                .addHeader("Authorization", credentials)
                .build();

        logger.debug("Authorization header added successfully, proceeding with the request");

        return chain.proceed(request);
    }
}

package pt.pchouse.pdf.api.sign.multicert;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.pchouse.pdf.api.sign.multicert.auth.JWTBearerAuth;

/**
 * The SignStashApiClient class acts as a client for interfacing with the SignStash API.
 * It extends the Multicert abstraction to provide additional implementations for creating
 * services and retrieving appropriate base URLs for different environments.
 * This client supports seamless switching between test and production environments.
 *
 * @since 1.0.0
 */
@Component()
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class SignStashApiClient extends Multicert
{
    /**
     * @since 1.0.0
     */
    private static final Logger logger = LoggerFactory.getLogger(SignStashApiClient.class);

    /**
     * Creates an instance of a service interface for API communication using the specified JWT
     * token for authentication and the specified environment configuration.
     *
     * @param <S>          the type of the service interface to be created
     * @param serviceClass the class of the service interface to be created
     * @param jwt          the JSON Web Token used for authentication in the `Authorization` header
     * @param isTest       a boolean flag that specifies whether to use the test or production environment.
     *                     If true, the base URL for the test environment will be used; otherwise, the production URL will be used.
     * @return an instance of the specified service interface type, configured with the provided JWT
     *         token for authentication and the specified environment.
     *
     * @since 1.0.0
     */
    public <S> S createService(Class<S> serviceClass, String jwt, boolean isTest) {
        logger.info("Creating service for: {} with isTest={}", serviceClass.getSimpleName(), isTest);
        return createService(serviceClass, new JWTBearerAuth(jwt), isTest);
    }

    /**
     * Retrieves the base URL for the SignStash API, based on the provided environment flag.
     *
     * @param isTest a boolean flag indicating whether the test environment should be used.
     *               If true, the base URL for the test environment is returned; otherwise,
     *               the production URL is returned.
     * @return a string representing the base URL for the specified environment.
     *
     * @since 1.0.0
     */
    public String getBaseUrl(boolean isTest)
    {
        String baseUrlPath = "/signstash/einvoice-integration-ws/";
        String url         = (isTest ? SERVER_API_TEST : SERVER_PRODUCTION) + baseUrlPath;
        logger.info("Base URL retrieved: {}", url);
        return url;
    }

}

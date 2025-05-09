package pt.pchouse.pdf.api.sign.multicert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pt.pchouse.pdf.api.sign.multicert.auth.HttpBasicAuth;
import pt.pchouse.pdf.api.sign.multicert.service.AuthenticateService;

/**
 * The SignStashApiAuthentication class extends the Multicert abstraction and provides
 * functionality for handling authentication-related services for the SignStash API.
 * This class is responsible for creating instances of the AuthenticateService
 * interface and configuring them for test or production environments based on the input parameters.
 * <p>
 * This class uses HTTP Basic Authentication to interact with the SignStash API and provides
 * utility methods to retrieve URLs for different environments.
 * <p>
 * It is developed as a Spring Component and is instantiated with singleton scope.
 *
 * @since 1.0.0
 */
@Component()
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class SignStashApiAuthentication extends Multicert
{
    /**
     * @since 1.0.0
     */
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Creates an instance of the {@code AuthenticateService} interface with the specified credentials
     * and environment configuration.
     * This method uses HTTP Basic Authentication to set up the service for API communication.
     *
     * @param username the username to be used for HTTP Basic Authentication
     * @param password the password corresponding to the username for authentication
     * @param isTest   a boolean flag indicating whether to configure the service for the test environment.
     *                 If true, the service will use a test server environment; otherwise, it will use
     *                 the production environment.
     * @return an instance of {@code AuthenticateService}, configured with the specified credentials and environment
     *
     * since 1.0.0
     */
    public AuthenticateService createService(String username, String password, boolean isTest) {
        logger.info("Creating service for username: {} in test environment: {}", username, isTest);
        return createService(
                AuthenticateService.class,
                new HttpBasicAuth(username, password),
                isTest
        );
    }

    /**
     * Retrieves the base URL for the SignStash API based on the specified environment.
     *
     * @param isTest a boolean flag indicating whether to retrieve the URL for the test environment.
     *               If true, the test server URL is returned; otherwise, the production server URL is returned.
     * @return the base URL of the SignStash API for the specified environment.
     *
     * @since 1.0.0
     */
    public String getBaseUrl(boolean isTest)
    {
        String url = isTest ? SERVER_API_TEST : SERVER_PRODUCTION;
        logger.info("Getting base URL for test environment: {}, URL: {}", isTest, url);
        return url;
    }

}

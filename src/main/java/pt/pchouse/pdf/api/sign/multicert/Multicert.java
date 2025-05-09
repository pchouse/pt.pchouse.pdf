package pt.pchouse.pdf.api.sign.multicert;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * The Multicert class serves as an abstraction for API integration with a focus on managing 
 * communication with the SignStash service. It provides methods and constants that facilitate 
 * creating API clients and switching between production and test environments.
 * 
 * @since 1.0.0
 */
public abstract class Multicert
{
    /**
     * URL of the production server for SignStash API integration.
     * This represents the base endpoint that should be used for all production-related API
     * communications with the SignStash service.
     */
    public final static String SERVER_PRODUCTION = "https://msignstash.multicert.com";

    /**
     * URL of the test server for SignStash API integration.
     * This represents the base endpoint to be used during development and testing phases
     * for API communication with the SignStash service.
     */
    public final static String SERVER_API_TEST = "https://staging.must.digital";

    /**
     * Logger instance for logging detailed information about API connections and behavior.
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Returns the base URL to be used for API communication.
     *
     * @param isTest a boolean flag indicating whether to return the base URL for the test environment or the production environment.
     *               If true, the test environment base URL is returned. If false, the production environment base URL is returned.
     * @return the base URL as a string based on the input parameter.
     */
    abstract public String getBaseUrl(boolean isTest);

    /**
     * Creates and returns an instance of a service interface for API communication using Retrofit.
     * This method configures the Retrofit client based on the given parameters and attaches
     * the necessary interceptor for making HTTP requests.
     *
     * @param <S>           the type of the service interface to be created
     * @param serviceClass  the class of the service interface to be created
     * @param interceptor   the interceptor to be added to the OkHttpClient for handling requests and responses
     * @param isTest        a boolean flag that specifies whether to use the test or production environment.
     *                      If true, the base URL for the test environment will be used; otherwise, the
     *                      production URL will be used.
     * @return an instance of the specified service interface type, configured with the given interceptor
     *                      and environment.
     *                      
     * @since 1.0.0
     */
    public <S> S createService(Class<S> serviceClass, Interceptor interceptor, boolean isTest) {

        logger.info("Entering createService with serviceClass: {}, isTest: {}", serviceClass.getSimpleName(), isTest);

        JSON                 json      = new JSON();
        OkHttpClient.Builder okBuilder = new OkHttpClient.Builder();

        okBuilder.addInterceptor(interceptor);

        OkHttpClient client = okBuilder.build();

        String baseUrl = getBaseUrl(isTest);
        logger.info("Using base URL: {}", baseUrl);

        Retrofit.Builder adapterBuilder = new Retrofit
                .Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonCustomConverterFactory.create(json.getGson()));

        S service = adapterBuilder
                .client(client)
                .build()
                .create(serviceClass);

        logger.info("Successfully created Retrofit service for class: {}", serviceClass.getSimpleName());

        return service;
    }

}

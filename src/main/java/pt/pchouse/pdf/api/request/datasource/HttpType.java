package pt.pchouse.pdf.api.request.datasource;

/**
 * Enum representing the HTTP request types supported in the API.
 * These types are commonly used for defining the method of communication between a client and server.
 * <p>
 * The available types are:
 * - GET: Typically used for retrieving data from the server.
 * - POST: Typically used for sending data to the server.
 * <p>
 * This enum is utilized throughout the API for specifying the desired HTTP method
 * in various communication scenarios, such as in server interactions or data exchange.
 *
 * @since 1.0.0
 */
public enum HttpType
{

    /**
     * Represents the HTTP GET request type.
     * GET is typically used for retrieving data from the server without
     * causing any modification to the server's state.
     */
    GET,

    /**
     * Represents the HTTP POST request type.
     * POST is typically used for sending data to the server, such as submitting
     * form data or uploading a file, and can result in changes to the server's state.
     *
     * @since 1.0.0
     */
    POST

}

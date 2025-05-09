package pt.pchouse.pdf.api.request.datasource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * Abstract class representing a server with common functionality such as managing
 * server URL and HTTP request type. This class serves as a foundation for specific
 * server implementations.
 *
 * @since 1.0.0
 */
public abstract class AServer extends APattern
{

    /**
     * @since 1.0.0
     */
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * This variable stores the server URL as a string.
     * It is used to define the endpoint for network communication in the server implementation.
     */
    private String url;

    /**
     * Represents the HTTP request type, such as GET or POST.
     * This variable is used to define the HTTP method for communication with the server endpoint.
     *
     * @since 1.0.0
     */
    private HttpType type;

    /**
     * Default constructor for the AServer class. This initializes a new instance
     * of the class and logs the creation for debugging purposes.
     *
     * @since 1.0.0
     */
    public AServer() {
        logger.debug("New instance of {}", this.getClass().getName());
    }

    /**
     * Retrieves the server URL.
     *
     * @return the current URL as a string
     *
     * @since 1.0.0
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the server URL to the specified value. The URL defines the endpoint
     * for network communication in the server implementation.
     *
     * @param url the new server URL as a string
     *
     * @since 1.0.0
     */
    public void setUrl(String url) {
        this.url = url;
        logger.debug(
                "URL set to {}",
                this.url == null ? "null" : this.url
        );
    }

    /**
     * Retrieves the current HTTP request type for the server, such as GET or POST.
     *
     * @return the HTTP request type
     *
     * @since 1.0.0
     */
    public HttpType getType() {
        return type;
    }

    /**
     * Sets the HTTP request type for the server, such as GET or POST.
     *
     * @param type the HTTP request type to be set
     *
     * @since 1.0.0
     */
    public void setType(HttpType type) {
        this.type = type;
        logger.debug(
                "Type set to {}",
                this.type == null ? "null" : this.type
        );
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AServer aServer = (AServer) o;
        return Objects.equals(getUrl(), aServer.getUrl()) && getType() == aServer.getType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUrl(), getType());
    }
}

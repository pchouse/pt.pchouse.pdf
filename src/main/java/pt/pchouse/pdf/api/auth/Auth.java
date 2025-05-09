package pt.pchouse.pdf.api.auth;

import inet.ipaddr.HostName;
import inet.ipaddr.IPAddressString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.Objects;

/**
 * The Auth class provides an implementation for managing client authorization
 * based on their IP address. It determines if a client request is authorized
 * by validating the client's remote IP address against a whitelist of allowed
 * IPs configured in the application properties file.
 * <p>
 * The class also captures the client's remote IP address from the web
 * container and provides utility methods for validation and access control.
 * <p>
 * This class is defined with a prototype scope, ensuring that each instance
 * of the class is independent of others.
 */
@Component
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
@PropertySources({
        @PropertySource("classpath:application.properties"),
        @PropertySource(value = "file:${apphome}/application.properties", ignoreResourceNotFound = true)
})
public class Auth implements IAuth
{
    /**
     * @since 1.0.0
     */
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Stores the remote client's IP address.
     * This field is set by the implementation of the `catchRemoteIP` method,
     * which retrieves the IP from the web container.
     * It is used to verify if the client is authorized to access the application.
     *
     * @since 1.0.0
     */
    private String remoteIp;

    /**
     * A property that holds a list of allowed client IP addresses.
     * These IP addresses are fetched from the `client.allowIps` entry
     * in the application configuration file.
     *
     * @since 1.0.0
     */
    @Value("${client.allowIps}")
    private String[] allowIps;

    /**
     * Constructs an instance of the Auth class.
     * This constructor is responsible for initializing and logging the allowed client IPs as configured.
     * <p>
     * It logs:
     * - The class name to indicate the creation of a new instance.
     * - The list of allowed client IPs provided in the configuration.
     * <p>
     * The allowed IPs are expected to be configured in the application properties,
     * and they determine which client IPs are authorized for access control.
     *
     * @since 1.0.0
     */
    public Auth() {
        logger.debug("New instance of {}", this.getClass().getName());
        logger.info("Client IPs allowed {}", Arrays.toString(allowIps));
    }

    /**
     * Retrieves the list of allowed client IP addresses.
     *
     * @return An array of strings representing the configured allowed IP addresses.
     *
     * @since 1.0.0
     */
    public String[] getAllowIps() {
        return allowIps;
    }

    /**
     * Captures the remote IP address of the client making the current HTTP request.
     * <p>
     * If the remote IP address has not been previously retrieved and stored, this method
     * obtains it from the current request context. The resolved IP address is stored in
     * the `remoteIp` field of the class for future use. Additionally, the method logs
     * the captured IP address using the class logger.
     * <p>
     * Functionality:
     * - Retrieves the HTTP request from the current request context.
     * - Extracts the remote IP address from the request.
     * - Stores the IP address in the class variable `remoteIp` if it's not already set.
     * - Logs the captured remote IP address.
     * <p>
     * Note:
     * - This method expects the method to run in a web application context where
     *   `RequestContextHolder` provides the current HTTP request attributes.
     *
     * @since 1.0.0
     */
    public void catchRemoteIP() {
        if (remoteIp == null) {
            remoteIp = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                    .getRequest()
                    .getRemoteAddr();
        }
        logger.info("Client remote IP {}", remoteIp);
    }

    /**
     * Retrieves the remote IP address of the client making the current request.
     *
     * @return A string representing the remote IP address.
     *
     * @since 1.0.0
     */
    @Override
    public String getRemoteIp() {
        return remoteIp;
    }

    /**
     * Get if the remote ip is authorized to make requests or not
     *
     * @return Check if the client remote IP is allowed
     * @since 1.0.0
     */
    public boolean isRemoteIpAllowed() {

        try {
            if (allowIps == null || allowIps.length == 0) {
                logger.warn("No client IPs configured, Allowing all");
                return true;
            }

            String ip = getRemoteIp();

            if (Arrays.asList(allowIps).contains(ip)) {
                logger.debug("Client IP {} is allowed", ip);
                return true;
            }

            logger.trace("Going to check if allowIps has localhost and if client from localhost");


            boolean allow = Arrays.stream(allowIps).anyMatch(
                    s -> new HostName(s).getAddress().contains(new IPAddressString(ip).getAddress())
            );

            if (allow) {
                logger.debug("Client IP {} is allowed", ip);
                return true;
            }

            logger.warn("Client IP {} is not allowed", ip);
            return false;
        } catch (Exception e) {
            logger.error("Error checking client IP: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Return if the client request is allowed or not.
     * In this implementation only verify if the remote is in the white list (application.config -> client.allowIps)
     *
     * @return Check if client is allowed
     * @since 1.0.0
     */
    @Override
    public boolean isNotAuthorized() {
        return !isRemoteIpAllowed();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Auth auth = (Auth) o;
        return Objects.equals(
                remoteIp, auth.getRemoteIp()) &&
                Arrays.equals(allowIps, auth.getAllowIps()
                );
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(remoteIp);
        result = 31 * result + Arrays.hashCode(allowIps);
        return result;
    }
}

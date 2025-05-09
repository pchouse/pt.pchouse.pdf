package pt.pchouse.pdf.api.auth;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Interface for handling authentication mechanisms.
 * The methods defined in this interface focus on
 * determining client authorization and managing remote IP capturing.
 * Implementations of this interface are responsible for defining specific
 * authentication strategies and behaviors.
 * <p>
 * This interface is designed to work in a web application context.
 */
@Component
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
public interface IAuth
{
    /**
     * Retrieves the remote IP address of the client making the current request.
     *
     * @return A string representing the remote IP address.
     *
     * @since 1.0.0
     */
    String getRemoteIp();

    /**
     * Get if teh request client is authorized or not
     *
     * @return If authorized
     * @since 1.0.0
     */
    boolean isNotAuthorized();

    /**
     * Captures the remote IP address of the client making the current HTTP request.
     *
     * This method retrieves the client's IP address from the current request context
     * and stores it in the `remoteIp` field. The captured IP address is then logged
     * for debugging or monitoring purposes. If the IP address has already been
     * captured, this method does not attempt to retrieve it again.
     *
     * Behavior:
     * - Extracts the current HTTP request from the request context.
     * - Retrieves the remote IP address associated with the client request.
     * - Stores the IP address in a local variable if not already defined.
     * - Logs the retrieved IP address.
     * <p>
     * Preconditions:
     * - Assumes this method is executed in a web application environment where
     *   request context is available.
     * <p>
     * Note: This method is context-dependent and utilizes Spring's
     * `RequestContextHolder` to access the active HTTP request.
     */
    void catchRemoteIP();
}

package pt.pchouse.pdf.api.sign.multicert.model;

import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Instant;

/**
 * AuthenticationResponse is a class representing the response from an authentication process.
 * <p>
 * This class is designed to parse and handle the JSON structure typically returned by an
 * authentication API. The fields in this class map to the elements in the JSON response.
 * Gson serialization and deserialization are supported through the use of annotations,
 * including custom adapters where necessary.
 * <p>
 * Fields included in the response:
 * - accessToken: The access token issued by the authentication server.
 * - tokenType: The type of token issued (e.g., "Bearer").
 * - expires: The expiration time of the token, represented as an Instant.
 * - scope: The scope of access granted by this token.
 * - jti: A unique identifier for the token.
 * <p>
 * This class provides getter and setter methods for each field. Each method includes logging
 * for debugging purposes to trace the values being set or retrieved.
 * <p>
 * A nested static class named JWTExpires is included, which serves as a custom Gson TypeAdapter
 * for handling the "expires_in" field in the JSON response. The adapter calculates expiration
 * in terms of the absolute Instant and manages serialization and deserialization accordingly.
 *
 * @since 1.0.0
 */
public class AuthenticationResponse
{
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * The access token used for authentication in API requests.
     * This token is typically issued by an OAuth2.0 server and is required
     * for making authorized requests to secure endpoints.
     *
     * @since 1.0.0
     */
    @SerializedName("access_token")
    private String accessToken;

    /**
     * Represents the type of the token in the authentication response.
     * Typically, it specifies the kind of token being issued, such as "Bearer".
     *
     * @since 1.0.0
     */
    @SerializedName("token_type")
    private String tokenType;

    /**
     * The `expires` field represents the expiration time of the JWT (JSON Web Token) in the form of an
     * {@link Instant}. It is deserialized and serialized using a custom Gson TypeAdapter,
     * {@code AuthenticationResponse.JWTExpires}.
     * <p>
     * The value is provided by the "expires_in" field in the JSON response, which specifies the number
     * of seconds until the token expires. The custom TypeAdapter, `AuthenticationResponse.JWTExpires`,
     * converts this value into an {@link Instant}.
     * <p>
     * This field is annotated with:
     * - `@SerializedName("expires_in")` to map the JSON property "expires_in".
     * - `@com.google.gson.annotations.JsonAdapter(AuthenticationResponse.JWTExpires.class)` to specify
     *   the use of the custom TypeAdapter for handling serialization and deserialization logic.
     *
     * @since 1.0.0
     */
    @SerializedName("expires_in")
    @com.google.gson.annotations.JsonAdapter(AuthenticationResponse.JWTExpires.class)
    private Instant expires;

    /**
     * The scope of the authorization, defining the access levels or permissions granted
     * to the client application as part of the OAuth authentication process.
     * This corresponds to the "scope" parameter in OAuth and defines the extent
     * of the access permissions associated with the access token.
     *
     * @since 1.0.0
     */
    @SerializedName("scope")
    private String scope;

    /**
     * Represents the unique identifier for a JSON Web Token (JWT).
     * This identifier can be used to uniquely identify a token in order to track it or manage its lifecycle.
     *
     * @since 1.0.0
     */
    @SerializedName("jti")
    private String jti;

    /**
     * Default constructor for the AuthenticationResponse class.
     * Initializes an instance of AuthenticationResponse with its default state and logs the initialization
     * details including the access token, token type, expiration time, scope, and JWT ID.
     *
     * @since 1.0.0
     */
    public AuthenticationResponse() {
        logger.debug("AuthenticationResponse initialized with initial state - accessToken: {}, tokenType: {}, expires: {}, scope: {}, jti: {}", accessToken, tokenType, expires, scope, jti);
    }

    /**
     * Retrieves the access token associated with the current authentication response.
     *
     * @return the access token as a string
     *
     * @since 1.0.0
     */
    public String getAccessToken() {
        return accessToken;
    }

    /**
     * Updates the access token for the current authentication response.
     *
     * @param accessToken the new access token to be set, replacing the existing one
     *
     * @since 1.0.0
     */
    public void setAccessToken(String accessToken) {
        logger.debug("Setting accessToken from {} to {}", this.accessToken, accessToken);
        this.accessToken = accessToken;
    }

    /**
     * Retrieves the type of token associated with the current authentication response.
     *
     * @return the token type as a string
     *
     * @since 1.0.0
     */
    public String getTokenType() {
        return tokenType;
    }

    /**
     * Sets the type of the token in the current authentication response.
     *
     * @param tokenType the new token type to be set, replacing the existing one
     *
     * @since 1.0.0
     */
    public void setTokenType(String tokenType) {
        logger.debug("Setting tokenType from {} to {}", this.tokenType, tokenType);
        this.tokenType = tokenType;
    }

    /**
     * Retrieves the expiration time of the current authentication response.
     *
     * @return the expiration time as an Instant object
     *
     * @since 1.0.0
     */
    public Instant getExpires() {
        return expires;
    }

    /**
     * Updates the expiration time for the current authentication response.
     *
     * @param expires the new expiration time to set, represented as an Instant
     *
     * @since 1.0.0
     */
    public void setExpires(Instant expires) {
        logger.debug("Setting expires from {} to {}", this.expires, expires);
        this.expires = expires;
    }

    /**
     * Retrieves the scope associated with the current authentication response.
     *
     * @return the scope as a string
     *
     * @since 1.0.0
     */
    public String getScope() {
        return scope;
    }

    /**
     * Sets the scope associated with the current authentication response.
     * This method updates the existing scope to the new value and logs the change.
     *
     * @param scope the new scope to be set
     *
     * @since 1.0.0
     */
    public void setScope(String scope) {
        logger.debug("Setting scope from {} to {}", this.scope, scope);
        this.scope = scope;
    }

    /**
     * Retrieves the JWT ID (JTI) associated with the current authentication response.
     *
     * @return the JWT ID as a string
     *
     * @since 1.0.0
     */
    public String getJti() {
        return jti;
    }

    /**
     * Updates the JWT ID (JTI) associated with the current authentication response.
     * This method sets the new JTI value while logging the change for debugging purposes.
     *
     * @param jti the new JWT ID to be set, replacing the existing value
     *
     * @since 1.0.0
     */
    public void setJti(String jti) {
        logger.debug("Setting jti from {} to {}", this.jti, jti);
        this.jti = jti;
    }

    /**
     * JWTExpires is a custom Gson TypeAdapter for serializing and deserializing JWT expiration times.
     * <p>
     * This class handles the representation of expiration times in JWTs by converting the "expires_in"
     * value (in seconds) to an Instant and vice versa. During serialization, this TypeAdapter calculates
     * the remaining time until expiration as the "expires_in" field. During deserialization, it converts
     * the "expires_in" value back into an absolute Instant.
     * <p>
     * Logging is provided to debug both the serialization and deserialization processes by utilizing
     * the logger for recording parsed and serialized values.
     * <p>
     * The class methods:
     * <p>
     * - write(JsonWriter out, Instant value): Serializes an Instant into a JSON object containing
     *   the "expires_in" field, representing the remaining time in seconds until expiration.
     * <p>
     * - read(JsonReader in): Deserializes a JSON object with an "expires_in" field into an Instant
     *   by adding the remaining seconds to the current time to compute the expiration Instant.
     *
     * @since 1.0.0
     */
    public static class JWTExpires extends TypeAdapter<Instant>
    {
        final Logger logger = LoggerFactory.getLogger(this.getClass());

        /**
         * Serializes an Instant into a JSON object representing the remaining time
         * until expiration in seconds, denoted by the "expires_in" field.
         *
         * @param out   the JsonWriter used to write the JSON output
         * @param value the Instant representing the expiration timestamp to be serialized
         * @throws IOException if an I/O error occurs while writing to the JsonWriter
         *
         * @since 1.0.0
         */
        @Override
        public void write(JsonWriter out, Instant value) throws IOException {
            long expiresIn = value.getEpochSecond() - Instant.now().getEpochSecond();
            logger.debug("Serializing expires_in as {} seconds from now", expiresIn);
            out.value(expiresIn);
        }

        /**
         * Deserializes a JSON object containing an "expires_in" field into an Instant.
         * <p>
         * This method reads the "expires_in" field, which represents the number of seconds
         * remaining until expiration, and calculates the expiration time by adding that
         * duration to the current time.
         *
         * @param in the JsonReader used to read the JSON input
         * @return an Instant representing the calculated expiration time
         * @throws IOException if an I/O error occurs while reading from the JsonReader
         *
         * @since 1.0.0
         */
        @Override
        public Instant read(JsonReader in) throws IOException {
            long    secondsUntilExpiry = in.nextLong();
            Instant expires            = Instant.now().plusSeconds(secondsUntilExpiry);
            logger.debug("Parsed expires_in as {} seconds, resulting in expiration time: {}", secondsUntilExpiry, expires);
            return expires;
        }
    }
}

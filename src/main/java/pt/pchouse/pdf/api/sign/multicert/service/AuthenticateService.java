package pt.pchouse.pdf.api.sign.multicert.service;

import okhttp3.ResponseBody;
import pt.pchouse.pdf.api.sign.multicert.model.AuthenticationResponse;
import retrofit2.Call;
import retrofit2.http.*;

/**
 * Interface representing an authentication service for handling OAuth2 token requests.
 * This service communicates with the authorization server to obtain an authentication token.
 * The request is made using the POST HTTP method with form URL encoding.
 *
 * @since 1.0.0
 */
public interface AuthenticateService
{
    /**
     * Sends a POST request to the OAuth2 authorization server to obtain an authentication token.
     * The request body must include the grant type as a form URL-encoded parameter.
     *
     * @param grantType The grant type indicating the type of OAuth2 flow to use (e.g., "client_credentials", "authorization_code").
     * @return A {@code Call<AuthenticationResponse>} object representing the pending HTTP request and response, which contains the authentication token.
     */
    @FormUrlEncoded
    // @Headers({"Content-Type: application/x-www-form-urlencoded"})
    @POST("/oauth2/authorization-server/oauth/token")
    Call<AuthenticationResponse> authenticate(
            @Field("grant_type") String grantType
    );

}

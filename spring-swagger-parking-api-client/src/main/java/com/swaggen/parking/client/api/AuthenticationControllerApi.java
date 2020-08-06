package com.swaggen.parking.client.api;

import com.swaggen.parking.client.invoker.ApiClientDecorator;
import com.swaggen.parking.client.model.AuthenticationRequest;
import com.swaggen.parking.client.model.ResponseEntity;
import com.swaggen.parking.client.model.SocialAuthRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

import javax.annotation.processing.Generated;

import static com.swaggen.parking.client.invoker.ApiEndpoints.auth;
import static com.swaggen.parking.client.invoker.ApiEndpoints.socialLogin;


@Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2020-08-06T11:26:27.336+03:00[EET]")
@Component("com.swaggen.parking.client.api.AuthenticationControllerApi")
public class AuthenticationControllerApi {
    private ApiClientDecorator apiClient;

    @Autowired
    public AuthenticationControllerApi(ApiClientDecorator apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClientDecorator getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClientDecorator apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * User authentication in system
     * Handles user authentication request from client web application
     * <p><b>200</b> - Success|OK
     * <p><b>500</b> - User doesn&#x27;t exists on the server
     *
     * @param body    Pass user credentials.
     * @param headers headers
     * @return ResponseEntity
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity authenticationUsingPOST(AuthenticationRequest body, Object headers) throws RestClientException {
        Object postBody = body;
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'body' when calling authenticationUsingPOST");
        }
        // verify the required parameter 'headers' is set
        if (headers == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'headers' when calling authenticationUsingPOST");
        }

        return apiClient.doGenericPostJsonRequest(headers, postBody, auth);
    }

    /**
     * User authentication in system by social provider id
     * Handles social user authentication request from client web application
     * <p><b>200</b> - Success|OK
     * <p><b>500</b> - Account associated with this social profile not found
     *
     * @param body    Account associated with this social profile not found
     * @param headers headers
     * @return ResponseEntity
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity socialLoginUsingPOST(SocialAuthRequest body, Object headers) throws RestClientException {
        Object postBody = body;
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'body' when calling socialLoginUsingPOST");
        }
        // verify the required parameter 'headers' is set
        if (headers == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'headers' when calling socialLoginUsingPOST");
        }

        return apiClient.doGenericPostJsonRequest(headers, postBody, auth + socialLogin);
    }
}

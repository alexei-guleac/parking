package com.swaggen.parking.client.api;

import com.swaggen.parking.client.invoker.ApiClientDecorator;
import com.swaggen.parking.client.model.ResponseEntity;
import com.swaggen.parking.client.model.SocialConnectRequest;
import com.swaggen.parking.client.model.SocialDisconnectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

import javax.annotation.processing.Generated;

import static com.swaggen.parking.client.invoker.ApiEndpoints.*;


@Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2020-08-06T11:26:27.336+03:00[EET]")
@Component("com.swaggen.parking.client.api.SocialControllerApi")
public class SocialControllerApi {
    private ApiClientDecorator apiClient;

    @Autowired
    public SocialControllerApi(ApiClientDecorator apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClientDecorator getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClientDecorator apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Method complements OAuth 2.0 flow with Github
     * GitHub redirects back to site with a temporary code in a code parameter as well as the state you provided in the previous step in a state parameter. The temporary code will expire after 10 minutes. This code needed for exchange to access token which is needed for app access the Github API
     * <p><b>200</b> - Success|OK
     * <p><b>500</b> - Code not provided
     *
     * @param headers headers
     * @return ResponseEntity
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity githubOAuthUsingGET(Object headers) throws RestClientException {
        // verify the required parameter 'headers' is set
        if (headers == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'headers' when calling githubOAuthUsingGET");
        }
        return apiClient.doGenericGetJsonRequest(headers, null, gitOAuth);
    }

    /**
     * Method complements OAuth 2.0 flow with Github
     * GitHub redirects back to site with a temporary code in a code parameter as well as the state you provided in the previous step in a state parameter. The temporary code will expire after 10 minutes. This code needed for exchange to access token which is needed for app access the Github API
     * <p><b>200</b> - Success|OK
     * <p><b>500</b> - Code not provided
     *
     * @param headers headers
     * @return ResponseEntity
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity githubOAuthUsingHEAD(Object headers) throws RestClientException {
        // verify the required parameter 'headers' is set
        if (headers == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'headers' when calling githubOAuthUsingHEAD");
        }
        return apiClient.doGenericHeadJsonRequest(headers, null, gitOAuth);
    }

    /**
     * Method complements OAuth 2.0 flow with Github
     * GitHub redirects back to site with a temporary code in a code parameter as well as the state you provided in the previous step in a state parameter. The temporary code will expire after 10 minutes. This code needed for exchange to access token which is needed for app access the Github API
     * <p><b>200</b> - Success|OK
     * <p><b>500</b> - Code not provided
     *
     * @param headers headers
     * @return ResponseEntity
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity githubOAuthUsingPOST(Object headers) throws RestClientException {
        // verify the required parameter 'headers' is set
        if (headers == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'headers' when calling githubOAuthUsingPOST");
        }

        return apiClient.doGenericPostJsonRequest(headers, null, gitOAuth);
    }

    /**
     * Method complements OAuth 2.0 flow with Github
     * GitHub redirects back to site with a temporary code in a code parameter as well as the state you provided in the previous step in a state parameter. The temporary code will expire after 10 minutes. This code needed for exchange to access token which is needed for app access the Github API
     * <p><b>200</b> - Success|OK
     * <p><b>500</b> - Code not provided
     *
     * @param headers headers
     * @return ResponseEntity
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity githubOAuthUsingPUT(Object headers) throws RestClientException {
        Object postBody = null;
        // verify the required parameter 'headers' is set
        if (headers == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'headers' when calling githubOAuthUsingPUT");
        }

        return apiClient.doGenericPutJsonRequest(headers, postBody, gitOAuth);
    }

    /**
     * Connect social provider to user account
     * Handles user social connection request from client web application
     * <p><b>200</b> - Success|OK
     * <p><b>500</b> - Another account is associated with this social profile
     *
     * @param body    Pass user and social provider information
     * @param headers headers
     * @return ResponseEntity
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity socialConnectUsingPOST(SocialConnectRequest body, Object headers) throws RestClientException {
        Object postBody = body;
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'body' when calling socialConnectUsingPOST");
        }
        // verify the required parameter 'headers' is set
        if (headers == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'headers' when calling socialConnectUsingPOST");
        }

        return apiClient.doGenericPostJsonRequest(headers, postBody, socialConnect);
    }

    /**
     * Disconnect social provider from user account
     * Handles user social disconnection request from client web application
     * <p><b>200</b> - Success|OK
     * <p><b>500</b> - User doesn&#x27;t exists on the server
     *
     * @param body    Pass user and social provider information
     * @param headers headers
     * @return ResponseEntity
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity socialDisconnectUsingPOST(SocialDisconnectRequest body, Object headers) throws RestClientException {
        Object postBody = body;
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'body' when calling socialDisconnectUsingPOST");
        }
        // verify the required parameter 'headers' is set
        if (headers == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'headers' when calling socialDisconnectUsingPOST");
        }

        return apiClient.doGenericPostJsonRequest(headers, postBody, socialDisconnect);
    }
}

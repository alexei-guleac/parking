package com.swaggen.parking.client.api;

import com.swaggen.parking.client.invoker.ApiClientDecorator;
import com.swaggen.parking.client.model.RegistrationRequest;
import com.swaggen.parking.client.model.ResponseEntity;
import com.swaggen.parking.client.model.SocialRegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

import javax.annotation.processing.Generated;

import static com.swaggen.parking.client.invoker.ApiEndpoints.*;


@Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2020-08-06T11:26:27.336+03:00[EET]")
@Component("com.swaggen.parking.client.api.RegistrationControllerApi")
public class RegistrationControllerApi {
    private ApiClientDecorator apiClient;

    @Autowired
    public RegistrationControllerApi(ApiClientDecorator apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClientDecorator getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClientDecorator apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * User registration in system
     * Handles user registration request from client web application
     * <p><b>200</b> - Success|OK
     * <p><b>500</b> - Account with this username already exists
     *
     * @param body    Pass user registration information.
     * @param headers headers
     * @return ResponseEntity
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity registrationUsingPOST(RegistrationRequest body, Object headers) throws RestClientException {
        Object postBody = body;
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'body' when calling registrationUsingPOST");
        }
        // verify the required parameter 'headers' is set
        if (headers == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'headers' when calling registrationUsingPOST");
        }
        return apiClient.doGenericPostJsonRequest(headers, postBody, register);
    }

    /**
     * User registration in system by social provider information
     * Handles social user registration request from client web application
     * <p><b>200</b> - Success|OK
     * <p><b>500</b> - Account with this social id already exists
     *
     * @param body    Pass user social registration information.
     * @param headers headers
     * @return ResponseEntity
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity socialRegistrationUsingHEAD(SocialRegisterRequest body, Object headers) throws RestClientException {
        Object postBody = body;
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'body' when calling socialRegistrationUsingHEAD");
        }
        // verify the required parameter 'headers' is set
        if (headers == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'headers' when calling socialRegistrationUsingHEAD");
        }
        return apiClient.doGenericHeadJsonRequest(headers, postBody, register + socialLogin);
    }

    /**
     * User registration in system by social provider information
     * Handles social user registration request from client web application
     * <p><b>200</b> - Success|OK
     * <p><b>500</b> - Account with this social id already exists
     *
     * @param body    Pass user social registration information.
     * @param headers headers
     * @return ResponseEntity
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity socialRegistrationUsingPATCH(SocialRegisterRequest body, Object headers) throws RestClientException {
        Object postBody = body;
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'body' when calling socialRegistrationUsingPATCH");
        }
        // verify the required parameter 'headers' is set
        if (headers == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'headers' when calling socialRegistrationUsingPATCH");
        }
        return apiClient.doGenericPatchJsonRequest(headers, postBody, register + socialLogin);
    }

    /**
     * User registration in system by social provider information
     * Handles social user registration request from client web application
     * <p><b>200</b> - Success|OK
     * <p><b>500</b> - Account with this social id already exists
     *
     * @param body    Pass user social registration information.
     * @param headers headers
     * @return ResponseEntity
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity socialRegistrationUsingPOST(SocialRegisterRequest body, Object headers) throws RestClientException {
        Object postBody = body;
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'body' when calling socialRegistrationUsingPOST");
        }
        // verify the required parameter 'headers' is set
        if (headers == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'headers' when calling socialRegistrationUsingPOST");
        }
        return apiClient.doGenericPostJsonRequest(headers, postBody, register + socialLogin);
    }

    /**
     * User registration in system by social provider information
     * Handles social user registration request from client web application
     * <p><b>200</b> - Success|OK
     * <p><b>500</b> - Account with this social id already exists
     *
     * @param body    Pass user social registration information.
     * @param headers headers
     * @return ResponseEntity
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity socialRegistrationUsingPUT(SocialRegisterRequest body, Object headers) throws RestClientException {
        Object postBody = body;
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'body' when calling socialRegistrationUsingPUT");
        }
        // verify the required parameter 'headers' is set
        if (headers == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'headers' when calling socialRegistrationUsingPUT");
        }
        return apiClient.doGenericPutJsonRequest(headers, postBody, register + socialLogin);
    }
}

package com.swaggen.parking.client.api;

import com.swaggen.parking.client.invoker.ApiClient;
import com.swaggen.parking.client.model.RegistrationRequest;
import com.swaggen.parking.client.model.ResponseEntity;
import com.swaggen.parking.client.model.SocialRegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.processing.Generated;
import java.util.List;


@Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2020-08-06T11:26:27.336+03:00[EET]")
@Component("com.swaggen.parking.client.api.RegistrationControllerApi")
public class RegistrationControllerApi {
    private ApiClient apiClient;

    public RegistrationControllerApi() {
        this(new ApiClient());
    }

    @Autowired
    public RegistrationControllerApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
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
        String path = UriComponentsBuilder.fromPath("/register").build().toUriString();

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();
        if (headers != null)
            headerParams.add("headers", apiClient.parameterToString(headers));

        final String[] accepts = {
                "*/*"
        };
        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final String[] contentTypes = {
                "application/json"
        };
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[]{"JWT"};

        ParameterizedTypeReference<ResponseEntity> returnType = new ParameterizedTypeReference<ResponseEntity>() {
        };
        return apiClient.invokeAPI(path, HttpMethod.POST, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
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
    public ResponseEntity socialRegistrationUsingDELETE(SocialRegisterRequest body, Object headers) throws RestClientException {
        Object postBody = body;
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'body' when calling socialRegistrationUsingDELETE");
        }
        // verify the required parameter 'headers' is set
        if (headers == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'headers' when calling socialRegistrationUsingDELETE");
        }
        String path = UriComponentsBuilder.fromPath("/register/social").build().toUriString();

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();
        if (headers != null)
            headerParams.add("headers", apiClient.parameterToString(headers));

        final String[] accepts = {
                "*/*"
        };
        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final String[] contentTypes = {
                "*/*"
        };
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[]{"JWT"};

        ParameterizedTypeReference<ResponseEntity> returnType = new ParameterizedTypeReference<ResponseEntity>() {
        };
        return apiClient.invokeAPI(path, HttpMethod.DELETE, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
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
    public ResponseEntity socialRegistrationUsingGET(SocialRegisterRequest body, Object headers) throws RestClientException {
        Object postBody = body;
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'body' when calling socialRegistrationUsingGET");
        }
        // verify the required parameter 'headers' is set
        if (headers == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'headers' when calling socialRegistrationUsingGET");
        }
        String path = UriComponentsBuilder.fromPath("/register/social").build().toUriString();

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();
        if (headers != null)
            headerParams.add("headers", apiClient.parameterToString(headers));

        final String[] accepts = {
                "*/*"
        };
        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final String[] contentTypes = {
                "*/*"
        };
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[]{"JWT"};

        ParameterizedTypeReference<ResponseEntity> returnType = new ParameterizedTypeReference<ResponseEntity>() {
        };
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
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
        String path = UriComponentsBuilder.fromPath("/register/social").build().toUriString();

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();
        if (headers != null)
            headerParams.add("headers", apiClient.parameterToString(headers));

        final String[] accepts = {
                "*/*"
        };
        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final String[] contentTypes = {
                "application/json"
        };
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[]{"JWT"};

        ParameterizedTypeReference<ResponseEntity> returnType = new ParameterizedTypeReference<ResponseEntity>() {
        };
        return apiClient.invokeAPI(path, HttpMethod.HEAD, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
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
    public ResponseEntity socialRegistrationUsingOPTIONS(SocialRegisterRequest body, Object headers) throws RestClientException {
        Object postBody = body;
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'body' when calling socialRegistrationUsingOPTIONS");
        }
        // verify the required parameter 'headers' is set
        if (headers == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'headers' when calling socialRegistrationUsingOPTIONS");
        }
        String path = UriComponentsBuilder.fromPath("/register/social").build().toUriString();

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();
        if (headers != null)
            headerParams.add("headers", apiClient.parameterToString(headers));

        final String[] accepts = {
                "*/*"
        };
        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final String[] contentTypes = {
                "application/json"
        };
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[]{"JWT"};

        ParameterizedTypeReference<ResponseEntity> returnType = new ParameterizedTypeReference<ResponseEntity>() {
        };
        return apiClient.invokeAPI(path, HttpMethod.OPTIONS, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
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
        String path = UriComponentsBuilder.fromPath("/register/social").build().toUriString();

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();
        if (headers != null)
            headerParams.add("headers", apiClient.parameterToString(headers));

        final String[] accepts = {
                "*/*"
        };
        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final String[] contentTypes = {
                "application/json"
        };
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[]{"JWT"};

        ParameterizedTypeReference<ResponseEntity> returnType = new ParameterizedTypeReference<ResponseEntity>() {
        };
        return apiClient.invokeAPI(path, HttpMethod.PATCH, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
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
        String path = UriComponentsBuilder.fromPath("/register/social").build().toUriString();

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();
        if (headers != null)
            headerParams.add("headers", apiClient.parameterToString(headers));

        final String[] accepts = {
                "*/*"
        };
        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final String[] contentTypes = {
                "application/json"
        };
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[]{"JWT"};

        ParameterizedTypeReference<ResponseEntity> returnType = new ParameterizedTypeReference<ResponseEntity>() {
        };
        return apiClient.invokeAPI(path, HttpMethod.POST, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
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
        String path = UriComponentsBuilder.fromPath("/register/social").build().toUriString();

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();
        if (headers != null)
            headerParams.add("headers", apiClient.parameterToString(headers));

        final String[] accepts = {
                "*/*"
        };
        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final String[] contentTypes = {
                "application/json"
        };
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[]{"JWT"};

        ParameterizedTypeReference<ResponseEntity> returnType = new ParameterizedTypeReference<ResponseEntity>() {
        };
        return apiClient.invokeAPI(path, HttpMethod.PUT, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
}

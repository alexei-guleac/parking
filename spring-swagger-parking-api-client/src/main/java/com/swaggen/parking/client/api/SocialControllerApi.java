package com.swaggen.parking.client.api;

import com.swaggen.parking.client.invoker.ApiClient;
import com.swaggen.parking.client.model.ResponseEntity;
import com.swaggen.parking.client.model.SocialConnectRequest;
import com.swaggen.parking.client.model.SocialDisconnectRequest;
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
@Component("com.swaggen.parking.client.api.SocialControllerApi")
public class SocialControllerApi {
    private ApiClient apiClient;

    public SocialControllerApi() {
        this(new ApiClient());
    }

    @Autowired
    public SocialControllerApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
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
    public ResponseEntity githubOAuthUsingDELETE(Object headers) throws RestClientException {
        Object postBody = null;
        // verify the required parameter 'headers' is set
        if (headers == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'headers' when calling githubOAuthUsingDELETE");
        }
        String path = UriComponentsBuilder.fromPath("/github_oauth").build().toUriString();

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
        Object postBody = null;
        // verify the required parameter 'headers' is set
        if (headers == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'headers' when calling githubOAuthUsingGET");
        }
        String path = UriComponentsBuilder.fromPath("/github_oauth").build().toUriString();

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
        Object postBody = null;
        // verify the required parameter 'headers' is set
        if (headers == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'headers' when calling githubOAuthUsingHEAD");
        }
        String path = UriComponentsBuilder.fromPath("/github_oauth").build().toUriString();

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
     * Method complements OAuth 2.0 flow with Github
     * GitHub redirects back to site with a temporary code in a code parameter as well as the state you provided in the previous step in a state parameter. The temporary code will expire after 10 minutes. This code needed for exchange to access token which is needed for app access the Github API
     * <p><b>200</b> - Success|OK
     * <p><b>500</b> - Code not provided
     *
     * @param headers headers
     * @return ResponseEntity
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity githubOAuthUsingOPTIONS(Object headers) throws RestClientException {
        Object postBody = null;
        // verify the required parameter 'headers' is set
        if (headers == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'headers' when calling githubOAuthUsingOPTIONS");
        }
        String path = UriComponentsBuilder.fromPath("/github_oauth").build().toUriString();

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
     * Method complements OAuth 2.0 flow with Github
     * GitHub redirects back to site with a temporary code in a code parameter as well as the state you provided in the previous step in a state parameter. The temporary code will expire after 10 minutes. This code needed for exchange to access token which is needed for app access the Github API
     * <p><b>200</b> - Success|OK
     * <p><b>500</b> - Code not provided
     *
     * @param headers headers
     * @return ResponseEntity
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity githubOAuthUsingPATCH(Object headers) throws RestClientException {
        Object postBody = null;
        // verify the required parameter 'headers' is set
        if (headers == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'headers' when calling githubOAuthUsingPATCH");
        }
        String path = UriComponentsBuilder.fromPath("/github_oauth").build().toUriString();

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
        Object postBody = null;
        // verify the required parameter 'headers' is set
        if (headers == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'headers' when calling githubOAuthUsingPOST");
        }
        String path = UriComponentsBuilder.fromPath("/github_oauth").build().toUriString();

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
        String path = UriComponentsBuilder.fromPath("/github_oauth").build().toUriString();

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
        String path = UriComponentsBuilder.fromPath("/social_connect").build().toUriString();

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
        String path = UriComponentsBuilder.fromPath("/social_disconnect").build().toUriString();

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
}

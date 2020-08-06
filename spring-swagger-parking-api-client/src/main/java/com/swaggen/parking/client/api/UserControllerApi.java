package com.swaggen.parking.client.api;

import com.swaggen.parking.client.invoker.ApiClient;
import com.swaggen.parking.client.model.ResponseEntity;
import com.swaggen.parking.client.model.SocialUser;
import com.swaggen.parking.client.model.UpdateUserRequest;
import com.swaggen.parking.client.model.UserLdap;
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
@Component("com.swaggen.parking.client.api.UserControllerApi")
public class UserControllerApi {
    private ApiClient apiClient;

    public UserControllerApi() {
        this(new ApiClient());
    }

    @Autowired
    public UserControllerApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Delete user
     * Endpoint to delete user profile from database
     * <p><b>200</b> - Success|OK
     * <p><b>500</b> - User doesn&#x27;t exists on the server
     *
     * @param headers headers
     * @return ResponseEntity
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity deleteUserUsingPOST(Object headers) throws RestClientException {
        Object postBody = null;
        // verify the required parameter 'headers' is set
        if (headers == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'headers' when calling deleteUserUsingPOST");
        }
        String path = UriComponentsBuilder.fromPath("/profile/delete").build().toUriString();

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
     * View a list of available employees
     * Endpoint to retrieve all users from database
     * <p><b>200</b> - Success|OK
     *
     * @return UserLdap
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public UserLdap getAllUsersUsingGET() throws RestClientException {
        Object postBody = null;
        String path = UriComponentsBuilder.fromPath("/users").build().toUriString();

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] accepts = {
                "*/*"
        };
        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final String[] contentTypes = {};
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[]{"JWT"};

        ParameterizedTypeReference<UserLdap> returnType = new ParameterizedTypeReference<UserLdap>() {
        };
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }

    /**
     * Get an employee by username
     * Endpoint to retrieve user profile from database
     * <p><b>200</b> - Success|OK
     * <p><b>500</b> - Server error
     *
     * @return SocialUser
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public SocialUser getUserByUsernameUsingPOST() throws RestClientException {
        Object postBody = null;
        String path = UriComponentsBuilder.fromPath("/profile").build().toUriString();

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] accepts = {
                "*/*"
        };
        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final String[] contentTypes = {
                "application/json"
        };
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[]{"JWT"};

        ParameterizedTypeReference<SocialUser> returnType = new ParameterizedTypeReference<SocialUser>() {
        };
        return apiClient.invokeAPI(path, HttpMethod.POST, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }

    /**
     * Update user
     * Endpoint to update user profile in database
     * <p><b>200</b> - Success|OK
     * <p><b>500</b> - User doesn&#x27;t exists on the server
     *
     * @param body    Pass user information for update
     * @param headers headers
     * @return ResponseEntity
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity updateUserUsingPOST(UpdateUserRequest body, Object headers) throws RestClientException {
        Object postBody = body;
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'body' when calling updateUserUsingPOST");
        }
        // verify the required parameter 'headers' is set
        if (headers == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'headers' when calling updateUserUsingPOST");
        }
        String path = UriComponentsBuilder.fromPath("/profile/update").build().toUriString();

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

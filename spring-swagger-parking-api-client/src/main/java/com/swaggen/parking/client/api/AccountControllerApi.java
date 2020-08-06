package com.swaggen.parking.client.api;

import com.swaggen.parking.client.invoker.ApiClient;
import com.swaggen.parking.client.model.ForgotPassRequest;
import com.swaggen.parking.client.model.ResetPasswordRequest;
import com.swaggen.parking.client.model.ResponseEntity;
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
@Component("com.swaggen.parking.client.api.AccountControllerApi")
public class AccountControllerApi {
    private ApiClient apiClient;

    public AccountControllerApi() {
        this(new ApiClient());
    }

    @Autowired
    public AccountControllerApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Confirm user account in the system.
     * This REST web service method will set user account to ENABLED if passed valid confirmation token.
     * <p><b>200</b> - Success|OK
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>500</b> - Confirmation token is expired. Register again
     *
     * @param body    Pass user account confirmation token.
     * @param headers headers
     * @return ResponseEntity
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity confirmUserAccountUsingPOST(String body, Object headers) throws RestClientException {
        Object postBody = body;
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'body' when calling confirmUserAccountUsingPOST");
        }
        // verify the required parameter 'headers' is set
        if (headers == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'headers' when calling confirmUserAccountUsingPOST");
        }
        String path = UriComponentsBuilder.fromPath("/confirm_account").build().toUriString();

        return getResponseEntity(headers, postBody, path);
    }

    private ResponseEntity getResponseEntity(Object headers, Object postBody, String path) {
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
     * User forgot password handler
     * Receives the user address and send an reset password email
     * <p><b>200</b> - Success|OK
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>500</b> - Password reset request is allowed only in specified period
     *
     * @param body    Pass user forgot password information.
     * @param headers headers
     * @return ResponseEntity
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity forgotUserPasswordUsingPOST(ForgotPassRequest body, Object headers) throws RestClientException {
        Object postBody = body;
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'body' when calling forgotUserPasswordUsingPOST");
        }
        // verify the required parameter 'headers' is set
        if (headers == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'headers' when calling forgotUserPasswordUsingPOST");
        }
        String path = UriComponentsBuilder.fromPath("/forgot_password").build().toUriString();

        return getResponseEntity(headers, postBody, path);
    }

    /**
     * ${AccountController.getRecaptcha.value}
     * ${AccountController.getRecaptcha.notes}
     * <p><b>200</b> - Success|OK
     * <p><b>500</b> - Token is empty or invalid
     *
     * @param body    gRecaptcha
     * @param headers headers
     * @return ResponseEntity
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity getRecaptchaUsingDELETE(String body, Object headers) throws RestClientException {
        Object postBody = body;
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'body' when calling getRecaptchaUsingDELETE");
        }
        // verify the required parameter 'headers' is set
        if (headers == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'headers' when calling getRecaptchaUsingDELETE");
        }
        String path = UriComponentsBuilder.fromPath("/validate_captcha").build().toUriString();

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
     * ${AccountController.getRecaptcha.value}
     * ${AccountController.getRecaptcha.notes}
     * <p><b>200</b> - Success|OK
     * <p><b>500</b> - Token is empty or invalid
     *
     * @param body    gRecaptcha
     * @param headers headers
     * @return ResponseEntity
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity getRecaptchaUsingGET(String body, Object headers) throws RestClientException {
        Object postBody = body;
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'body' when calling getRecaptchaUsingGET");
        }
        // verify the required parameter 'headers' is set
        if (headers == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'headers' when calling getRecaptchaUsingGET");
        }
        String path = UriComponentsBuilder.fromPath("/validate_captcha").build().toUriString();

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
     * ${AccountController.getRecaptcha.value}
     * ${AccountController.getRecaptcha.notes}
     * <p><b>200</b> - Success|OK
     * <p><b>500</b> - Token is empty or invalid
     *
     * @param body    gRecaptcha
     * @param headers headers
     * @return ResponseEntity
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity getRecaptchaUsingHEAD(String body, Object headers) throws RestClientException {
        Object postBody = body;
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'body' when calling getRecaptchaUsingHEAD");
        }
        // verify the required parameter 'headers' is set
        if (headers == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'headers' when calling getRecaptchaUsingHEAD");
        }
        String path = UriComponentsBuilder.fromPath("/validate_captcha").build().toUriString();

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
     * ${AccountController.getRecaptcha.value}
     * ${AccountController.getRecaptcha.notes}
     * <p><b>200</b> - Success|OK
     * <p><b>500</b> - Token is empty or invalid
     *
     * @param body    gRecaptcha
     * @param headers headers
     * @return ResponseEntity
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity getRecaptchaUsingOPTIONS(String body, Object headers) throws RestClientException {
        Object postBody = body;
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'body' when calling getRecaptchaUsingOPTIONS");
        }
        // verify the required parameter 'headers' is set
        if (headers == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'headers' when calling getRecaptchaUsingOPTIONS");
        }
        String path = UriComponentsBuilder.fromPath("/validate_captcha").build().toUriString();

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
     * ${AccountController.getRecaptcha.value}
     * ${AccountController.getRecaptcha.notes}
     * <p><b>200</b> - Success|OK
     * <p><b>500</b> - Token is empty or invalid
     *
     * @param body    gRecaptcha
     * @param headers headers
     * @return ResponseEntity
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity getRecaptchaUsingPATCH(String body, Object headers) throws RestClientException {
        Object postBody = body;
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'body' when calling getRecaptchaUsingPATCH");
        }
        // verify the required parameter 'headers' is set
        if (headers == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'headers' when calling getRecaptchaUsingPATCH");
        }
        String path = UriComponentsBuilder.fromPath("/validate_captcha").build().toUriString();

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
     * ${AccountController.getRecaptcha.value}
     * ${AccountController.getRecaptcha.notes}
     * <p><b>200</b> - Success|OK
     * <p><b>500</b> - Token is empty or invalid
     *
     * @param body    gRecaptcha
     * @param headers headers
     * @return ResponseEntity
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity getRecaptchaUsingPOST(String body, Object headers) throws RestClientException {
        Object postBody = body;
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'body' when calling getRecaptchaUsingPOST");
        }
        // verify the required parameter 'headers' is set
        if (headers == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'headers' when calling getRecaptchaUsingPOST");
        }
        String path = UriComponentsBuilder.fromPath("/validate_captcha").build().toUriString();

        return getResponseEntity(headers, postBody, path);
    }

    /**
     * ${AccountController.getRecaptcha.value}
     * ${AccountController.getRecaptcha.notes}
     * <p><b>200</b> - Success|OK
     * <p><b>500</b> - Token is empty or invalid
     *
     * @param body    gRecaptcha
     * @param headers headers
     * @return ResponseEntity
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity getRecaptchaUsingPUT(String body, Object headers) throws RestClientException {
        Object postBody = body;
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'body' when calling getRecaptchaUsingPUT");
        }
        // verify the required parameter 'headers' is set
        if (headers == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'headers' when calling getRecaptchaUsingPUT");
        }
        String path = UriComponentsBuilder.fromPath("/validate_captcha").build().toUriString();

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
     * Endpoint to reset a user&#x27;s password
     * Reset user password based on received confirmation token
     * <p><b>200</b> - Success|OK
     * <p><b>401</b> - Unauthorized
     * <p><b>403</b> - Forbidden
     * <p><b>500</b> - Password change is allowed only in specified period
     *
     * @param body    Pass user reset password information.
     * @param headers headers
     * @return ResponseEntity
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity resetUserPasswordUsingPOST(ResetPasswordRequest body, Object headers) throws RestClientException {
        Object postBody = body;
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'body' when calling resetUserPasswordUsingPOST");
        }
        // verify the required parameter 'headers' is set
        if (headers == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'headers' when calling resetUserPasswordUsingPOST");
        }
        String path = UriComponentsBuilder.fromPath("/reset_password").build().toUriString();

        return getResponseEntity(headers, postBody, path);
    }
}

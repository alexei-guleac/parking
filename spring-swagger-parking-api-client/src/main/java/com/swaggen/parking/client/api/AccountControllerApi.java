package com.swaggen.parking.client.api;

import com.swaggen.parking.client.invoker.ApiClientDecorator;
import com.swaggen.parking.client.model.ForgotPassRequest;
import com.swaggen.parking.client.model.ResetPasswordRequest;
import com.swaggen.parking.client.model.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

import javax.annotation.processing.Generated;

import static com.swaggen.parking.client.invoker.ApiEndpoints.*;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.WILDCARD;


@Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2020-08-06T11:26:27.336+03:00[EET]")
@Component("com.swaggen.parking.client.api.AccountControllerApi")
public class AccountControllerApi {
    private ApiClientDecorator apiClient;

    @Autowired
    public AccountControllerApi(ApiClientDecorator apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClientDecorator getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClientDecorator apiClient) {
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

        return apiClient.doGenericPostJsonRequest(headers, postBody, confirmAction);
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

        return apiClient.doGenericPostJsonRequest(headers, postBody, forgotPassword);
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

        return apiClient.doGetJsonRequest(
                headers, postBody, validateCaptcha, new String[]{WILDCARD}, new String[]{WILDCARD});
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

        return apiClient.doHeadJsonRequest(
                headers, postBody, validateCaptcha, new String[]{WILDCARD}, new String[]{WILDCARD});
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

        return apiClient.doPostJsonRequest(
                headers, postBody, validateCaptcha, new String[]{WILDCARD}, new String[]{WILDCARD});
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

        return apiClient.doPostJsonRequest(
                headers, postBody, resetPassword, new String[]{WILDCARD}, new String[]{APPLICATION_JSON});
    }
}

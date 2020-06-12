package com.isd.parking.web.rest.controllers.account;

import com.isd.parking.services.AccountService;
import com.isd.parking.web.rest.ApiEndpoints;
import com.isd.parking.web.rest.payload.account.ForgotPassRequest;
import com.isd.parking.web.rest.payload.account.ResetPasswordRequest;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.POST;


/**
 * Provides methods for
 * - Google captcha processing,
 * - user account confirmation
 * - user account forgot and reset password handle
 */
@Api(value = "Account Controller",
    description = "Operations pertaining to user account")
@RestController
@CrossOrigin(origins = "*")
@Slf4j
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * Handles Google re-captcha flow
     *
     * @param gRecaptcha - captcha data from client web application
     * @return HTTP response with user validation error or success details
     * sample message
     * {
     * "success": true,
     * "challenge_ts": "2020-03-07T13:40:25Z",
     * "hostname": "localhost"
     * }
     */
    @ApiOperation(value = "${AccountController.getRecaptcha.value}",
        response = ResponseEntity.class,
        notes = "${AccountController.getRecaptcha.notes}")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Success|OK"),
        @ApiResponse(code = 500, message = "Token is empty or invalid")
    })
    @ApiImplicitParams(
        @ApiImplicitParam(name = "getRecaptcha",
            value = "${AccountController.getRecaptcha.getRecaptcha}",
            required = true, dataType = "string")
    )
    @ResponseBody
    @RequestMapping(ApiEndpoints.validateCaptcha)
    public @NotNull ResponseEntity<?> getRecaptcha(@RequestBody String gRecaptcha,
                                                   @RequestHeader Map<String, String> headers) {
        return accountService.getRecaptcha(gRecaptcha, headers);
    }

    /**
     * User account confirmation handler
     *
     * @param confirmRequest - user account confirmation token
     * @return HTTP response with user validation error or success details
     */
    @ApiOperation(value = "${AccountController.confirmUserAccount.value}",
        response = ResponseEntity.class,
        notes = "${AccountController.confirmUserAccount.notes}")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Success|OK"),
        @ApiResponse(code = 401, message = "Unauthorized"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 500, message = "Confirmation token is expired. Register again")
    })
    @ApiImplicitParams(
        @ApiImplicitParam(name = "confirmRequest",
            value = "${AccountController.confirmUserAccount.confirmationToken}",
            required = true, dataType = "string")
    )
    @RequestMapping(value = ApiEndpoints.confirmAction, method = POST)
    public @NotNull ResponseEntity<?> confirmUserAccount(@RequestBody String confirmRequest,
                                                         @RequestHeader Map<String, String> headers) {
        return accountService.confirmUserAccount(confirmRequest, headers);
    }

    /**
     * User forgot password handler receives the user address and send an reset password email
     *
     * @param request - forgot password initial request contains user email and device data for set email language
     * @return HTTP response with user forgot password initial processing error or success email send details
     * @throws IOException
     * @throws MessagingException
     */
    @ApiOperation(value = "${AccountController.forgotUserPassword.value}",
        response = ResponseEntity.class,
        notes = "${AccountController.forgotUserPassword.notes}")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Success|OK"),
        @ApiResponse(code = 401, message = "Unauthorized"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 500, message = "Password reset request is allowed only in specified period")
    })
    @ApiImplicitParams(
        @ApiImplicitParam(name = "request",
            value = "${AccountController.forgotUserPassword.forgotPassRequest}",
            required = true, dataType = "ForgotPassRequest")
    )
    @RequestMapping(value = ApiEndpoints.forgotPassword, method = POST)
    public @NotNull ResponseEntity<?> forgotUserPassword(@RequestBody @NotNull ForgotPassRequest request,
                                                         @RequestHeader Map<String, String> headers)
        throws IOException, MessagingException {

        return accountService.forgotUserPassword(request, headers);
    }

    /**
     * Endpoint to reset a user's password
     *
     * @param resetPasswordRequest - request contains all necessary user data - confirmation token
     *                             for retrieve user uid and check validation period,
     *                             new password and device information
     * @return HTTP response with user password reset processing error or success details
     */
    @ApiOperation(value = "${AccountController.resetUserPassword.value}",
        response = ResponseEntity.class,
        notes = "${AccountController.resetUserPassword.notes}")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Success|OK"),
        @ApiResponse(code = 401, message = "Unauthorized"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 500, message = "Password change is allowed only in specified period")
    })
    @ApiImplicitParams(
        @ApiImplicitParam(name = "resetPasswordRequest",
            value = "${AccountController.resetUserPassword.resetPasswordRequest}",
            required = true, dataType = "ResetPasswordRequest")
    )
    @RequestMapping(value = ApiEndpoints.resetPassword, method = POST)
    public @NotNull ResponseEntity<?> resetUserPassword(@RequestBody @NotNull ResetPasswordRequest resetPasswordRequest,
                                                        @RequestHeader Map<String, String> headers) {
        return accountService.resetUserPassword(resetPasswordRequest, headers);
    }
}

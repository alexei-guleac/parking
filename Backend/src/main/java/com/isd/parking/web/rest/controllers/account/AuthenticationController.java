package com.isd.parking.web.rest.controllers.account;

import com.isd.parking.services.AuthenticationService;
import com.isd.parking.web.rest.ApiEndpoints;
import com.isd.parking.web.rest.payload.account.auth.AuthenticationRequest;
import com.isd.parking.web.rest.payload.account.auth.SocialAuthRequest;
import com.nimbusds.jose.JOSEException;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.POST;


/**
 * Provides methods for
 * - user authentication
 * - social login
 * - Github Oauth
 */
@RestController
@RequestMapping(ApiEndpoints.auth)
@Slf4j
@Api(value = "Authentication Controller",
    description = "Operations pertaining to user authentication in system")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * Handles user authentication request from client web application
     *
     * @param authenticationRequest - contains user credentials
     * @return HTTP Response with authentication error details or success with JWT token
     * JWT Token sample
     * * {
     * * "alg":"HS256",
     * * "typ":"JWT"
     * * }
     * * {
     * * "username": "user",
     * * "role": "admin",
     * * "iat": 1556172533,
     * * "exp": 1556173133
     * * }
     * * HMACSHA256(
     * * base64UrlEncode(header) + "." +
     * * base64UrlEncode(payload),
     * * SECRET!
     * * )
     * @throws AuthenticationException - if wrong credentials passed
     * @throws JOSEException           - if provided corrupted server secret sign
     */
    @ApiOperation(value = "${AuthenticationController.authentication.value}",
        response = ResponseEntity.class,
        notes = "${AuthenticationController.authentication.notes}")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Success|OK"),
        @ApiResponse(code = 500, message = "User doesn't exists on the server")
    })
    @ApiImplicitParams(
        @ApiImplicitParam(name = "authenticationRequest",
            value = "${AuthenticationController.authentication.authenticationRequest}",
            required = true, dataType = "AuthenticationRequest")
    )
    @RequestMapping(method = POST)
    public @NotNull ResponseEntity<?> authentication(@RequestBody @NotNull AuthenticationRequest authenticationRequest,
                                                     @RequestHeader Map<String, String> headers)
        throws AuthenticationException, JOSEException {

        return authenticationService.authentication(authenticationRequest, headers);
    }

    /**
     * Handles user social authentication request from client web application
     *
     * @param socialAuthRequest - contains social user id and specifies social service provider
     * @return HTTP Response with authentication error details or success with JWT token
     * @throws JOSEException - if provided corrupted server secret sign
     */
    @ApiOperation(value = "${AuthenticationController.socialLogin.value}",
        response = ResponseEntity.class,
        notes = "${AuthenticationController.socialLogin.notes}")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Success|OK"),
        @ApiResponse(code = 500, message = "Account associated with this social profile not found")
    })
    @ApiImplicitParams(
        @ApiImplicitParam(name = "socialAuthRequest",
            value = "${AuthenticationController.socialLogin.socialAuthRequest}",
            required = true, dataType = "SocialAuthRequest")
    )
    @RequestMapping(value = ApiEndpoints.socialLogin, method = POST)
    public @NotNull ResponseEntity<?> socialLogin(@RequestBody @NotNull SocialAuthRequest socialAuthRequest,
                                                  @RequestHeader Map<String, String> headers)
        throws JOSEException {

        return authenticationService.socialLogin(socialAuthRequest, headers);
    }
}

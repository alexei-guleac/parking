package com.isd.parking.web.rest.controllers.account;

import com.isd.parking.config.locale.SmartLocaleResolver;
import com.isd.parking.models.enums.AccountState;
import com.isd.parking.models.users.UserLdap;
import com.isd.parking.security.JwtUtils;
import com.isd.parking.storage.ldap.UserServiceImpl;
import com.isd.parking.web.rest.ApiEndpoints;
import com.isd.parking.web.rest.payload.ResponseEntityFactory;
import com.isd.parking.web.rest.payload.account.auth.AuthenticationRequest;
import com.isd.parking.web.rest.payload.account.auth.AuthenticationResponse;
import com.isd.parking.web.rest.payload.account.auth.SocialAuthRequest;
import com.isd.parking.web.rest.payload.account.auth.SocialAuthResponse;
import com.nimbusds.jose.JOSEException;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Locale;
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

    private final AuthenticationManager authenticationManager;

    private final UserServiceImpl userService;

    private final JwtUtils jwtUtils;

    private final ResourceBundleMessageSource messageSource;

    private final SmartLocaleResolver localeResolver;

    private final ResponseEntityFactory responseEntityFactory;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager,
                                    UserServiceImpl userService,
                                    JwtUtils jwtUtils,
                                    ResourceBundleMessageSource messageSource,
                                    SmartLocaleResolver localeResolver,
                                    ResponseEntityFactory responseEntityFactory) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtUtils = jwtUtils;
        this.messageSource = messageSource;
        this.localeResolver = localeResolver;
        this.responseEntityFactory = responseEntityFactory;
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

        final AuthenticationRequest.AuthDetails credentials = authenticationRequest.getAuthDetails();
        final String username = credentials.getUsername();
        final String password = credentials.getPassword();
        final Locale locale = localeResolver.resolveLocale(headers);

        // check if user exists
        final UserLdap userFound = userService.findById(username);
        if (userFound != null) {
            assertAccountEnabled(userFound, locale);

            @NotNull final UsernamePasswordAuthenticationToken userAuthToken =
                new UsernamePasswordAuthenticationToken(username, password);
            // throws authenticationException if it fails!
            final Authentication authentication = authenticationManager.authenticate(userAuthToken);
            if (userAuthToken.isAuthenticated()) {
                SecurityContextHolder.getContext().setAuthentication(userAuthToken);
            }

            final String token = jwtUtils.generateSignedJWTToken(username, authentication);
            // Return the token
            return ResponseEntity.ok(new AuthenticationResponse(token));
        } else {
            return responseEntityFactory.userNotExists(locale);
        }
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

        final String id = socialAuthRequest.getId();
        final String provider = socialAuthRequest.getSocialProvider();
        final UserLdap userFound = userService.getUserBySocialId(id, provider);
        final Locale locale = localeResolver.resolveLocale(headers);

        if (userFound != null) {
            assertAccountEnabled(userFound, locale);

            final String username = userFound.getUid();
            final String authorities = userService.getAuthoritiesMembershipById(username);
            final String token = jwtUtils.generateSignedJWTToken(username, authorities);

            // Return the token
            return ResponseEntity.ok(new SocialAuthResponse(token, username));
        } else {
            return responseEntityFactory.socialNotExists(locale);
        }
    }

    /**
     * Checks if user account is enabled
     *
     * @param user - target user
     * @param locale - user specified locale
     */
    private void assertAccountEnabled(@NotNull UserLdap user, Locale locale) {
        if (user.getAccountState() != AccountState.ENABLED) {
            if (user.getAccountState() == AccountState.WAITING_CONFIRMATION) {
                throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    messageSource.getMessage("AuthenticationController.not-confirmed", null, locale));
            } else if (user.getAccountState() == AccountState.DISABLED) {
                throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    messageSource.getMessage("AuthenticationController.user-disabled", null, locale));
            }
        }
    }
}

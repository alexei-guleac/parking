package com.isd.parking.controller.web.account;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Splitter;
import com.isd.parking.models.enums.AccountState;
import com.isd.parking.models.users.UserLdap;
import com.isd.parking.security.model.payload.auth.*;
import com.isd.parking.service.RestService;
import com.isd.parking.storage.ldap.UserServiceImpl;
import com.isd.parking.utils.AppFileUtils;
import com.nimbusds.jose.JOSEException;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

import static com.isd.parking.controller.ApiEndpoints.*;
import static com.isd.parking.security.JwtUtils.generateHMACToken;
import static com.isd.parking.utils.AppJsonUtils.getJsonStringFromObject;
import static org.springframework.web.bind.annotation.RequestMethod.POST;


/**
 * Provides methods for
 * - user authentication
 * - social login
 * - Github Oauth
 */
@RestController
@RequestMapping(auth)
@Slf4j
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;

    private final UserServiceImpl userService;

    private final RestService restService;

    private final String secretKeyFile = "secret.key";

    private final String secret = new AppFileUtils().getResourceAsString(secretKeyFile);

    @Value("${spring.ldap.base}")
    private String ldapSearchBase;

    // JWT token expiration period in minutes
    private final int expirationInMinutes = 72 * 60;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager,
                                    UserServiceImpl userService,
                                    RestService restService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.restService = restService;
    }

    /**
     * Handles user authentication request from client web application
     *
     * @param authenticationRequest - contains user credentials
     * @return HTTP Response with authentication error details or success with JWT token
     * JWT Token sample
     * {
     * "alg":"HS256",
     * "typ":"JWT"
     * }
     * {
     * "username": "user",
     * "role": "admin",
     * "iat": 1556172533,
     * "exp": 1556173133
     * }
     * HMACSHA256(
     * base64UrlEncode(header) + "." +
     * base64UrlEncode(payload),
     * SECRET!
     * )
     * @throws AuthenticationException
     * @throws JOSEException
     */
    @RequestMapping(method = POST)
    public ResponseEntity<?> authenticationRequest(@RequestBody AuthenticationRequest authenticationRequest)
        throws AuthenticationException, JOSEException {

        AuthenticationRequest.AuthDetails credentials = authenticationRequest.getAuthDetails();
        final String username = credentials.getUsername();
        final String password = credentials.getPassword();

        // check if user exists
        UserLdap userFound = userService.findById(username);
        if (userFound != null) {
            assertAccountEnabled(userFound);

            UsernamePasswordAuthenticationToken userAuthToken = new UsernamePasswordAuthenticationToken(username, password);
            // throws authenticationException if it fails!
            Authentication authentication = authenticationManager.authenticate(userAuthToken);
            if (userAuthToken.isAuthenticated()) {
                SecurityContextHolder.getContext().setAuthentication(userAuthToken);
            }

            final String token = generateSignedJWTToken(username, authentication);
            // Return the token
            return ResponseEntity.ok(new AuthenticationResponse(token));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("User doesn't exists on the server");
        }
    }

    /**
     * Handles user social authentication request from client web application
     *
     * @param socialAuthRequest - contains social user id and specifies social service provider
     * @return HTTP Response with authentication error details or success with JWT token
     * @throws JOSEException
     */
    @RequestMapping(value = social, method = POST)
    public ResponseEntity<?> socialLogin(@RequestBody SocialAuthRequest socialAuthRequest) throws JOSEException {

        final String id = socialAuthRequest.getId();
        final String provider = socialAuthRequest.getSocialProvider();
        final UserLdap userFound = userService.getUserBySocialId(id, provider);

        if (userFound != null) {
            assertAccountEnabled(userFound);

            final String username = userFound.getUid();
            String authorities = userService.getAuthoritiesMembershipById(username);
            final String token = generateSignedJWTToken(username, authorities);

            // Return the token
            return ResponseEntity.ok(new SocialAuthResponse(token, username));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Account associated with this social profile not found");
        }
    }

    /**
     * Method provides OAuth 2.0 flow with Github
     *
     * @param githubOAuthCode - GitHub redirects back to site with a temporary code in a code parameter
     *                        as well as the state you provided in the previous step in a state parameter.
     *                        The temporary code will expire after 10 minutes.
     *                        This code needed for exchange to access token which is needed for app access the Github API
     * @return HTTP Response with error details or success with Github API access token
     * @throws JsonProcessingException
     */
    @ResponseBody
    @RequestMapping(gitOAuth)
    public ResponseEntity<?> githubOAuth(@RequestBody String githubOAuthCode) throws JsonProcessingException {

        final String code = new JSONObject(githubOAuthCode).getString("code");
        if (code == null || code.equals("")) {
            return ResponseEntity.ok("Code not provided");
        } else {
            String url = GithubOauthRequest.GithubOauthConstants.TOKEN_URL;
            String requestJsonString = getJsonStringFromObject(new GithubOauthRequest(code));
            String response = restService.postPlainJSON(url, requestJsonString);

            @SuppressWarnings("UnstableApiUsage") final Map<String, String> parameters = Splitter.on('&').trimResults().withKeyValueSeparator('=').split(response);

            return ResponseEntity.ok(new GithubOauthResponse(parameters.get("access_token")));
        }
    }

    /**
     * Checks if user account is enabled
     *
     * @param user - target user
     */
    private void assertAccountEnabled(UserLdap user) {
        if (user.getAccountState() != AccountState.ENABLED) {
            if (user.getAccountState() == AccountState.WAITING_CONFIRMATION) {
                throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Confirm registration. There should be a letter in the mail with a confirmation link.");
            } else if (user.getAccountState() == AccountState.DISABLED) {
                throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "User with this email disabled on the server. Contact with administrator.");
            }
        }
    }

    /**
     * Generates signed JWT Token from Authentication object
     *
     * @param username       - user id
     * @param authentication - Authentication object
     * @return signed JWT Token
     * @throws JOSEException
     */
    private String generateSignedJWTToken(String username, Authentication authentication) throws JOSEException {
        return generateHMACToken(username, authentication.getAuthorities(), secret, expirationInMinutes);
    }

    /**
     * Generates signed JWT Token with specified roles
     *
     * @param username - user id
     * @param roles    - user roles String
     * @return signed JWT Token
     * @throws JOSEException
     */
    private String generateSignedJWTToken(String username, String roles) throws JOSEException {
        return generateHMACToken(username, roles, secret, expirationInMinutes);
    }
}

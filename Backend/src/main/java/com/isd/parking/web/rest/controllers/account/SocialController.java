package com.isd.parking.web.rest.controllers.account;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Splitter;
import com.isd.parking.config.SwaggerConfig;
import com.isd.parking.models.users.User;
import com.isd.parking.models.users.UserLdap;
import com.isd.parking.models.users.UserMapper;
import com.isd.parking.services.RestService;
import com.isd.parking.storage.ldap.UserServiceImpl;
import com.isd.parking.web.rest.ApiEndpoints;
import com.isd.parking.web.rest.payload.auth.GithubOauthRequest;
import com.isd.parking.web.rest.payload.auth.GithubOauthResponse;
import com.isd.parking.web.rest.payload.connect.SocialConnectRequest;
import com.isd.parking.web.rest.payload.connect.SocialConnectResponse;
import com.isd.parking.web.rest.payload.connect.SocialDisconnectRequest;
import com.isd.parking.web.rest.payload.connect.SocialDisconnectResponse;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static com.isd.parking.utilities.AppJsonUtils.getJsonStringFromObject;
import static org.springframework.web.bind.annotation.RequestMethod.POST;


/**
 * Provides methods for
 * - user authentication
 * - social login
 * - Github Oauth
 */
@RestController
@Slf4j
@Api(value = "Social Controller",
    description = "Operations pertaining to social providers connection")
public class SocialController {

    private final RestService restService;

    private final UserServiceImpl userService;

    private final UserMapper userMapper;

    @Autowired
    public SocialController(RestService restService,
                            UserServiceImpl userService,
                            UserMapper userMapper) {
        this.restService = restService;
        this.userService = userService;
        this.userMapper = userMapper;
    }

    /**
     * Method complements OAuth 2.0 flow with Github
     *
     * @param githubOAuthCode - GitHub redirects back to site with a temporary code in a code parameter
     *                        as well as the state you provided in the previous step in a state parameter.
     *                        The temporary code will expire after 10 minutes.
     *                        This code needed for exchange to access token which is needed for app access the Github API
     * @return HTTP Response with error details or success with Github API access token
     * @throws JsonProcessingException - if JSON parsing error
     */
    @ApiOperation(value = "${SocialController.githubOAuth.value}",
        response = ResponseEntity.class,
        notes = "${SocialController.githubOAuth.notes}")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Success|OK"),
        @ApiResponse(code = 500, message = "Code not provided")
    })
    @ApiImplicitParams(
        @ApiImplicitParam(name = "githubOAuthCode",
            value = "${SocialController.githubOAuth.githubOAuthCode}",
            required = true, dataType = "String")
    )
    @ResponseBody
    @RequestMapping(ApiEndpoints.gitOAuth)
    public @NotNull ResponseEntity<?> githubOAuth(@RequestBody String githubOAuthCode)
        throws JsonProcessingException {

        final String code = new JSONObject(githubOAuthCode).getString("code");
        if (code == null || code.equals("")) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Code not provided");
        } else {
            @NotNull String url = GithubOauthRequest.GithubOauthConstants.TOKEN_URL;
            String requestJsonString = getJsonStringFromObject(new GithubOauthRequest(code));
            String response = restService.postPlainJSON(url, requestJsonString);
            assert response != null;
            @SuppressWarnings("UnstableApiUsage") final @NotNull Map<String, String> parameters =
                Splitter.on('&').trimResults().withKeyValueSeparator('=').split(response);

            return ResponseEntity.ok(new GithubOauthResponse(parameters.get("access_token")));
        }
    }

    /**
     * Handles user social connection request from client web application
     *
     * @param socialConnectRequest - contains social user id and specifies social service provider
     * @return HTTP Response with connection success or error details
     */
    @ApiOperation(value = "${SocialController.socialConnect.value}",
        response = ResponseEntity.class,
        notes = "${SocialController.socialConnect.notes}",
        authorizations = {@Authorization(value = SwaggerConfig.TOKEN_TYPE)})
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Success|OK"),
        @ApiResponse(code = 500, message = "Another account is associated with this social profile")
    })
    @ApiImplicitParams(
        @ApiImplicitParam(name = "socialConnectRequest",
            value = "${SocialController.socialConnect.socialConnectRequest}",
            required = true, dataType = "SocialConnectRequest")
    )
    @RequestMapping(value = ApiEndpoints.socialConnect, method = POST)
    public @NotNull ResponseEntity<?> socialConnect(@RequestBody @NotNull SocialConnectRequest socialConnectRequest) {

        final User user = socialConnectRequest.getUser();
        final String email = user.getEmail();
        final String id = socialConnectRequest.getId();
        final String provider = socialConnectRequest.getSocialProvider();
        final String username = socialConnectRequest.getUsername();

        log.info(socialConnectRequest.toString());
        if (email != null) {
            final UserLdap existedUser = userService.getUserByEmail(email);
            // same emails not allowed
            if (existedUser != null) {
                // case when confirmation expired and confirm impossible anymore
                if (existedUser.accountConfirmationIsExpired()) {
                    userService.deleteUser(userMapper.userToUserLdap(user));
                }
                if (existedUser.accountConfirmationValid()) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Social account with this email already exists and waiting for confirmation");
                }
                // allow same email for current requested account
                if (!existedUser.getUid().equals(username)) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Social account with this email already exists");
                }
            }
        }

        UserLdap userFound = userService.findById(username);
        if (userFound != null) {
            userFound = userService.getUserBySocialId(id, provider);

            if (userFound == null) {
                userService.connectSocialProvider(username, provider, id);

                // verify operation success
                userFound = userService.getUserBySocialId(id, provider);
                if (userFound != null) {
                    // Return the result
                    return ResponseEntity.ok(new SocialConnectResponse(true));
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Something went wrong during social connection. Contact support");
                }
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Another account is associated with this social profile");
            }
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("User doesn't exists on the server");
        }
    }

    /**
     * Handles user social disconnection request from client web application
     *
     * @param socialDisconnectRequest - contains social user service provider
     * @return HTTP Response with disconnection success or error details
     */
    @ApiOperation(value = "${SocialController.socialDisconnect.value}",
        response = ResponseEntity.class,
        notes = "${SocialController.socialDisconnect.notes}",
        authorizations = {@Authorization(value = SwaggerConfig.TOKEN_TYPE)})
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Success|OK"),
        @ApiResponse(code = 500, message = "User doesn't exists on the server")
    })
    @ApiImplicitParams(
        @ApiImplicitParam(name = "socialDisconnectRequest",
            value = "${SocialController.socialDisconnect.socialDisconnectRequest}",
            required = true, dataType = "SocialDisconnectRequest")
    )
    @RequestMapping(value = ApiEndpoints.socialDisconnect, method = POST)
    public @NotNull ResponseEntity<?> socialDisconnect(@RequestBody @NotNull SocialDisconnectRequest socialDisconnectRequest) {

        final String provider = socialDisconnectRequest.getSocialProvider();
        final String username = socialDisconnectRequest.getUsername();

        final UserLdap userFound = userService.findById(username);
        if (userFound != null) {
            userService.disconnectSocialProvider(username, provider);
            // Return the result
            return ResponseEntity.ok(new SocialDisconnectResponse(true));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("User doesn't exists on the server");
        }
    }
}
package com.isd.parking.web.rest.controllers.account;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.isd.parking.config.SwaggerConfig;
import com.isd.parking.services.SocialService;
import com.isd.parking.web.rest.ApiEndpoints;
import com.isd.parking.web.rest.payload.account.connect.SocialConnectRequest;
import com.isd.parking.web.rest.payload.account.connect.SocialDisconnectRequest;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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

    private final SocialService socialService;

    @Autowired
    public SocialController(SocialService socialService) {
        this.socialService = socialService;
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
    public @NotNull ResponseEntity<?> githubOAuth(@RequestBody String githubOAuthCode,
                                                  @RequestHeader Map<String, String> headers)
        throws JsonProcessingException {

        return socialService.githubOAuth(githubOAuthCode, headers);
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
    public @NotNull ResponseEntity<?> socialConnect(@RequestBody @NotNull SocialConnectRequest socialConnectRequest,
                                                    @RequestHeader Map<String, String> headers) {

        return socialService.socialConnect(socialConnectRequest, headers);
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
    public @NotNull ResponseEntity<?> socialDisconnect(@RequestBody @NotNull SocialDisconnectRequest socialDisconnectRequest,
                                                       @RequestHeader Map<String, String> headers) {

        return socialService.socialDisconnect(socialDisconnectRequest, headers);
    }
}

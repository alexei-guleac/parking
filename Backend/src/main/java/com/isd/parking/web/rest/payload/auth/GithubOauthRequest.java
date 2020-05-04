package com.isd.parking.web.rest.payload.auth;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;


@Data
@RequiredArgsConstructor
@ApiModel(description = "Github OAuth 2.0 information. ")
public class GithubOauthRequest {

    @JsonProperty()
    @JsonAlias({"client_id"})
    @ApiModelProperty(notes = "The client ID received from GitHub when OAuth client application is registered.",
        required = true)
    @NotBlank
    @NonNull
    private String client_id;

    @JsonProperty()
    @JsonAlias({"client_secret"})
    @ApiModelProperty(notes = "The client secret received from GitHub for GitHub API client App",
        required = true)
    @NotBlank
    @NonNull
    private String client_secret;

    @JsonProperty()
    @JsonAlias({"code"})
    @ApiModelProperty(notes = "The temporary code received from web application after authorizing request" +
        "The temporary code will expire after 10 minutes."
        , required = true)
    @NotBlank
    @NonNull
    private String code;

    public GithubOauthRequest(String code) {
        this.code = code;
        client_id = GithubOauthConstants.CLIENT_ID;
        client_secret = GithubOauthConstants.CLIENT_SECRET;
    }

    public static class GithubOauthConstants {
        // ===========================================
        // Constants necessary for Oauth 2.0 in Github server
        // ===========================================
        static final String CLIENT_ID = "9454ba3084a75c484cbe";

        static final String CLIENT_SECRET = "5783f0a98aadd4260ed061fa9b6a019f91e70f9f";

        public static final String TOKEN_URL = "https://github.com/login/oauth/access_token";
    }
}

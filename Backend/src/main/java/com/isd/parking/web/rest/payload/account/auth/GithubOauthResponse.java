package com.isd.parking.web.rest.payload.account.auth;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;


@Data
@ApiModel(description = "Github OAuth 2.0 server access token response. ")
public class GithubOauthResponse {

    @JsonProperty()
    @JsonAlias({"access_token"})
    @ApiModelProperty(notes = "The access token allows you to make requests to the Github API on a behalf of a user",
        required = true)
    @NotBlank
    @NonNull
    private String access_token;

}

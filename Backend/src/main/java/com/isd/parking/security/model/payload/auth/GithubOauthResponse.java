package com.isd.parking.security.model.payload.auth;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;


@Data
public class GithubOauthResponse {

    @JsonProperty()
    @JsonAlias({"access_token"})
    @NotBlank
    @NonNull
    private String access_token;

}

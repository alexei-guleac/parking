package com.isd.parking.web.rest.payload.auth;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;


@Data
@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
@ApiModel(description = "Authentication with social provider server response. ")
public class SocialAuthResponse extends AuthenticationResponse {

    @JsonProperty()
    @JsonAlias({"username"})
    @ApiModelProperty(notes = "Authenticated user username")
    @NotBlank
    @NonNull
    private String username;

    public SocialAuthResponse(String token, String username) {
        super(token);
        this.username = username;
    }
}

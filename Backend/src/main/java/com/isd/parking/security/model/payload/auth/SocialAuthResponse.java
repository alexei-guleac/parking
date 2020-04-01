package com.isd.parking.security.model.payload.auth;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;


@EqualsAndHashCode(callSuper = true)
@Data
@RequiredArgsConstructor
public class SocialAuthResponse extends AuthenticationResponse {

    @JsonProperty()
    @JsonAlias({"username"})
    @NotBlank
    @NonNull
    private String username;

    public SocialAuthResponse(String token, String username) {
        super(token);
        this.username = username;
    }
}

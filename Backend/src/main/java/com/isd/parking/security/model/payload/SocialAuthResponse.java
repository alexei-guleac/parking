package com.isd.parking.security.model.payload;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@RequiredArgsConstructor
public class SocialAuthResponse extends AuthenticationResponse {

    @NotBlank
    @NonNull
    private String username;

    public SocialAuthResponse(String token, String username) {
        super(token);
        this.username = username;
    }
}

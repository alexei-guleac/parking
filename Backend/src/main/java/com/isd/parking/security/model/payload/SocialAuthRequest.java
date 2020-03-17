package com.isd.parking.security.model.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SocialAuthRequest {

    @NotBlank
    @NonNull
    private String id;

    @NotBlank
    @NonNull
    private String socialProvider;

    public enum SocialAuthProvider {
        local,
        facebook,
        google,
        github
    }
}

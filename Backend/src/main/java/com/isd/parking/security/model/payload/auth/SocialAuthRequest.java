package com.isd.parking.security.model.payload.auth;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SocialAuthRequest {

    @JsonProperty()
    @JsonAlias({"id"})
    @NotBlank
    @NonNull
    private String id;

    @JsonProperty()
    @JsonAlias({"socialProvider"})
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

package com.isd.parking.security.model.payload.auth;


import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.isd.parking.security.model.payload.register.DeviceInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequest {

    @JsonProperty()
    @JsonAlias({"credentials"})
    @NotBlank
    @NonNull
    private AuthDetails authDetails;

    @JsonProperty()
    @JsonAlias({"deviceInfo"})
    @NotBlank
    @NonNull
    private DeviceInfo deviceInfo;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class AuthDetails {

        @JsonProperty()
        @JsonAlias({"username"})
        @NotBlank
        @NonNull
        private String username;

        @JsonProperty()
        @JsonAlias({"password"})
        @NotBlank
        @NonNull
        private String password;

    }
}

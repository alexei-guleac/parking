package com.isd.parking.security.model.payload;

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
public class ResetPasswordRequest {

    @JsonProperty()
    @JsonAlias({"resetRequest"})
    @NotBlank
    @NonNull
    private ResetDetails resetDetails;

    @JsonProperty()
    @JsonAlias({"deviceInfo"})
    @NotBlank
    @NonNull
    private DeviceInfo deviceInfo;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResetDetails {
        @JsonProperty()
        @JsonAlias({"confirmationToken"})
        @NotBlank
        @NonNull
        private String confirmationToken;

        @JsonProperty()
        @JsonAlias({"password"})
        @NotBlank
        @NonNull
        private String password;

    }

}

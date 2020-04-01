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
public class ForgotPassRequest {

    @JsonProperty()
    @JsonAlias({"email"})
    @NotBlank
    @NonNull
    private String email;

    @JsonProperty()
    @JsonAlias({"deviceInfo"})
    @NotBlank
    @NonNull
    private DeviceInfo deviceInfo;
}

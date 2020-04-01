package com.isd.parking.security.model.payload.register;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.isd.parking.models.users.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationRequest {

    @JsonProperty()
    @JsonAlias({"user"})
    @NotBlank
    @NonNull
    private User user;

    @JsonProperty()
    @JsonAlias({"deviceInfo"})
    @NotBlank
    @NonNull
    private DeviceInfo deviceInfo;
}

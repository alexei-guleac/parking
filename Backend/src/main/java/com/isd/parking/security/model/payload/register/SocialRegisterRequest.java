package com.isd.parking.security.model.payload.register;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.isd.parking.models.users.User;
import com.isd.parking.security.model.payload.auth.SocialAuthRequest;
import lombok.*;

import javax.validation.constraints.NotBlank;


@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class SocialRegisterRequest extends SocialAuthRequest {

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

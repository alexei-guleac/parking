package com.isd.parking.web.rest.payload.account.register;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.isd.parking.models.users.User;
import com.isd.parking.web.rest.payload.DeviceInfo;
import com.isd.parking.web.rest.payload.account.auth.SocialAuthRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;


@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "User registration with social service provider. ")
public class SocialRegisterRequest extends SocialAuthRequest {

    @JsonProperty()
    @JsonAlias({"user"})
    @ApiModelProperty(notes = "User registration information", required = true)
    @NotBlank
    @NonNull
    private User user;

    @JsonProperty()
    @JsonAlias({"deviceInfo"})
    @ApiModelProperty(notes = "User device information (for region language targeting purpose). ")
    @NotBlank
    @NonNull
    private DeviceInfo deviceInfo;

}

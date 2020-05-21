package com.isd.parking.web.rest.payload.account.auth;


import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.isd.parking.web.rest.payload.DeviceInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "Authentication user information. ")
public class AuthenticationRequest {

    @JsonProperty()
    @JsonAlias({"credentials"})
    @ApiModelProperty(notes = "Authentication user details (username and password). ", required = true)
    @NotBlank
    @NonNull
    private AuthDetails authDetails;

    @JsonProperty()
    @JsonAlias({"deviceInfo"})
    @ApiModelProperty(notes = "User device information (for region language targeting purpose). ")
    @NotBlank
    @NonNull
    private DeviceInfo deviceInfo;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel(description = "Authentication user details. ")
    public class AuthDetails {

        @JsonProperty()
        @JsonAlias({"username"})
        @ApiModelProperty(notes = "Username", required = true)
        @NotBlank
        @NonNull
        private String username;

        @JsonProperty()
        @JsonAlias({"password"})
        @ApiModelProperty(notes = "User password", required = true)
        @NotBlank
        @NonNull
        private String password;

    }
}

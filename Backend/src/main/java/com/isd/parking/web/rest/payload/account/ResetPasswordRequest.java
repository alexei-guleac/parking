package com.isd.parking.web.rest.payload.account;

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
@ApiModel(description = "Request for user password reset. ")
public class ResetPasswordRequest {

    @JsonProperty()
    @JsonAlias({"resetRequest"})
    @ApiModelProperty(notes = "User password reset details. ", required = true)
    @NotBlank
    @NonNull
    private ResetDetails resetDetails;

    @JsonProperty()
    @JsonAlias({"deviceInfo"})
    @ApiModelProperty(notes = "User device information (for region language targeting purpose). ")
    @NotBlank
    @NonNull
    private DeviceInfo deviceInfo;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel(description = "User password reset details. ")
    public static class ResetDetails {

        @JsonProperty()
        @JsonAlias({"confirmationToken"})
        @ApiModelProperty(notes = "Confirmation token for verification. ", required = true)
        @NotBlank
        @NonNull
        private String confirmationToken;

        @JsonProperty()
        @JsonAlias({"password"})
        @ApiModelProperty(notes = "New password. ", required = true)
        @NotBlank
        @NonNull
        private String password;

    }

}

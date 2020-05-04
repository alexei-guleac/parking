package com.isd.parking.web.rest.payload;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
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
@ApiModel(description = "Forgot user password request. ")
public class ForgotPassRequest {

    @JsonProperty()
    @JsonAlias({"email"})
    @ApiModelProperty(notes = "User email. ", required = true)
    @NotBlank
    @NonNull
    private String email;

    @JsonProperty()
    @JsonAlias({"deviceInfo"})
    @ApiModelProperty(notes = "User device information (for region language targeting purpose). ")
    @NotBlank
    @NonNull
    private DeviceInfo deviceInfo;
}

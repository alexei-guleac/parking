package com.isd.parking.web.rest.payload.auth;


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
@ApiModel(description = "Authentication server response. ")
public class AuthenticationResponse {

    @JsonProperty()
    @JsonAlias({"token"})
    @ApiModelProperty(notes = "JWT created from user credentials", required = true)
    @NotBlank
    @NonNull
    private String token;

}

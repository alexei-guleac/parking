package com.isd.parking.web.rest.payload.account.connect;

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
@ApiModel(description = "Social provider profile disconnection request. ")
public class SocialDisconnectRequest {

    @JsonProperty()
    @JsonAlias({"username"})
    @ApiModelProperty(notes = "User username", required = true)
    @NotBlank
    @NonNull
    private String username;

    @JsonProperty()
    @JsonAlias({"socialProvider"})
    @ApiModelProperty(notes = "Social provider short name", required = true)
    @NotBlank
    @NonNull
    private String socialProvider;

}

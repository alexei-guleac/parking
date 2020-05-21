package com.isd.parking.web.rest.payload.account.connect;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.isd.parking.models.users.User;
import com.isd.parking.web.rest.payload.account.auth.SocialAuthRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;


@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "Social provider profile connect request. ")
public class SocialConnectRequest extends SocialAuthRequest {

    @JsonProperty()
    @JsonAlias({"username"})
    @ApiModelProperty(notes = "User username", required = true)
    @NotBlank
    @NonNull
    private String username;

    @JsonProperty()
    @JsonAlias({"user"})
    @ApiModelProperty(notes = "User information", required = true)
    @NotBlank
    @NonNull
    private User user;

}

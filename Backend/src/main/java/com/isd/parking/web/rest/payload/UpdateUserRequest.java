package com.isd.parking.web.rest.payload;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.isd.parking.models.users.User;
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
@ApiModel(description = "Request for user information update. ")
public class UpdateUserRequest {

    @JsonProperty()
    @JsonAlias({"user"})
    @ApiModelProperty(notes = "User information", required = true)
    @NotBlank
    @NonNull
    private User user;

    @JsonProperty()
    @JsonAlias({"username"})
    @ApiModelProperty(notes = "User username", required = true)
    @NotBlank
    @NonNull
    private String username;
}

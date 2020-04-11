package com.isd.parking.security.model.payload;

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
public class UpdateUserRequest {

    @JsonProperty()
    @JsonAlias({"user"})
    @NotBlank
    @NonNull
    private User user;

    @JsonProperty()
    @JsonAlias({"username"})
    @NotBlank
    @NonNull
    private String username;
}

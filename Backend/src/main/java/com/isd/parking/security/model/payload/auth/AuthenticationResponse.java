package com.isd.parking.security.model.payload.auth;


import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {

    @JsonProperty()
    @JsonAlias({"token"})
    @NotBlank
    @NonNull
    private String token;

}

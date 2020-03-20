package com.isd.parking.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.Email;

@Data
public class BaseEmailUser {
    @JsonProperty()
    @JsonAlias({"email"})
    @Email
    String email;
}

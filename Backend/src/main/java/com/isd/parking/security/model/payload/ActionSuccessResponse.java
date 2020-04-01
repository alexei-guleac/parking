package com.isd.parking.security.model.payload;

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
public class ActionSuccessResponse {

    @JsonProperty()
    @JsonAlias({"success"})
    @NotBlank
    @NonNull
    private boolean success;

}

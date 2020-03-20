package com.isd.parking.security.model.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class ResetPasswordRequest {

    @NotBlank
    @NonNull
    private String confirmationToken;

    @NotBlank
    @NonNull
    private String password;

}

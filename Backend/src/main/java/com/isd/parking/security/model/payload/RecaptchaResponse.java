package com.isd.parking.security.model.payload;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
@Builder
public class RecaptchaResponse {

    @NotBlank
    @NonNull
    private boolean success;

    @NotBlank
    @NonNull
    private Date challenge_ts;

    @NotBlank
    @NonNull
    private String hostname;

    private String message;

}

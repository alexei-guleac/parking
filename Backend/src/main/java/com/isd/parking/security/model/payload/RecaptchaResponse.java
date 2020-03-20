package com.isd.parking.security.model.payload;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class RecaptchaResponse extends ActionSuccessResponse {

    @NotBlank
    @NonNull
    private Date challenge_ts;

    @NotBlank
    @NonNull
    private String hostname;

    private String message;

    @Builder
    public RecaptchaResponse(@NotBlank @NonNull Date challenge_ts, @NotBlank @NonNull String hostname, String message) {
        this.challenge_ts = challenge_ts;
        this.hostname = hostname;
        this.message = message;
    }

    @Builder
    public RecaptchaResponse(@NotBlank @NonNull boolean success, @NotBlank @NonNull Date challenge_ts, @NotBlank @NonNull String hostname, String message) {
        super(success);
        this.challenge_ts = challenge_ts;
        this.hostname = hostname;
        this.message = message;
    }

    public static class GRecaptcha {

        // the secret key from google developer admin console;
        public static final String SECRET_KEY = "6Lcngd8UAAAAAGP04a5nTLgeZKXDWKasd-A1rHHQ";

        // token validation url is URL: https://www.google.com/recaptcha/api/siteverify (Google ReCaptcha v2)
        // METHOD used is: POST
        public static final String GRECAPTCHA_API_URL = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s";
    }
}

package com.isd.parking.web.rest.payload.account;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.isd.parking.web.rest.payload.ActionSuccessResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import java.util.Date;


@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "Google recaptcha result response. ")
public class RecaptchaResponse extends ActionSuccessResponse {

    @JsonProperty()
    @JsonAlias({"challenge_ts"})
    @ApiModelProperty(notes = "Operation timestamp. ", required = true)
    @NotBlank
    @NonNull
    private Date challenge_ts;

    @JsonProperty()
    @JsonAlias({"hostname"})
    @ApiModelProperty(notes = "Provider hostname. ", required = true)
    @NotBlank
    @NonNull
    private String hostname;

    @JsonProperty()
    @JsonAlias({"message"})
    @ApiModelProperty(notes = "Processing result message. ", required = true)
    private String message;

    @Builder
    public RecaptchaResponse(@NotBlank @NonNull Date challenge_ts,
                             @NotBlank @NonNull String hostname, String message) {
        this.challenge_ts = challenge_ts;
        this.hostname = hostname;
        this.message = message;
    }

    @Builder
    public RecaptchaResponse(@NotBlank @NonNull boolean success,
                             @NotBlank @NonNull Date challenge_ts,
                             @NotBlank @NonNull String hostname, String message) {
        super(success);
        this.challenge_ts = challenge_ts;
        this.hostname = hostname;
        this.message = message;
    }

    public static class GoogleRecaptchaConstants {

        // the secret key from Google developer admin console;
        public static final String SECRET_KEY = "6Lcngd8UAAAAAGP04a5nTLgeZKXDWKasd-A1rHHQ";

        // token validation url is URL: https://www.google.com/recaptcha/api/siteverify (Google ReCaptcha v2)
        // METHOD used is: POST
        public static final String GRECAPTCHA_API_URL = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s";
    }
}

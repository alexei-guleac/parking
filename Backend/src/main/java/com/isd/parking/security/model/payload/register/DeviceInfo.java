package com.isd.parking.security.model.payload.register;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;


/**
 * Client web application user device information (for language and region targeting purpose)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceInfo {

    @JsonProperty()
    @JsonAlias({"platformType"})
    @NotBlank
    @NonNull
    private String platformType;

    @JsonProperty()
    @JsonAlias({"browser"})
    @NotBlank
    @NonNull
    private String browser;

    @JsonProperty()
    @JsonAlias({"language"})
    @NotBlank
    @NonNull
    private String language;

    @JsonProperty()
    @JsonAlias({"os"})
    @NotBlank
    @NonNull
    private String os;

    @JsonProperty()
    @JsonAlias({"timezone"})
    @NotBlank
    @NonNull
    private String timezone;

}

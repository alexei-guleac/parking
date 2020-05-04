package com.isd.parking.web.rest.payload;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(description = "user device information (for region language and timezone detect). ")
public class DeviceInfo {

    @JsonProperty()
    @JsonAlias({"platformType"})
    @ApiModelProperty(notes = "Device type (desktop, mobile)")
    @NotBlank
    @NonNull
    private String platformType;

    @JsonProperty()
    @JsonAlias({"browser"})
    @ApiModelProperty(notes = "Browser name")
    @NotBlank
    @NonNull
    private String browser;

    @JsonProperty()
    @JsonAlias({"language"})
    @ApiModelProperty(notes = "Device language (ru, en)")
    @NotBlank
    @NonNull
    private String language;

    @JsonProperty()
    @JsonAlias({"os"})
    @ApiModelProperty(notes = "Operational system name (Windows, Linux)")
    @NotBlank
    @NonNull
    private String os;

    @JsonProperty()
    @JsonAlias({"timezone"})
    @ApiModelProperty(notes = "Device timezone (region, time)")
    @NotBlank
    @NonNull
    private String timezone;

}

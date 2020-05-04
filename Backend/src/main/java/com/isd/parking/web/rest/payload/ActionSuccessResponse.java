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


@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "Success server operation general response. ")
public class ActionSuccessResponse {

    @JsonProperty()
    @JsonAlias({"success"})
    @ApiModelProperty(notes = "Success server operation general response status. ", required = true)
    @NotBlank
    @NonNull
    private boolean success;

}

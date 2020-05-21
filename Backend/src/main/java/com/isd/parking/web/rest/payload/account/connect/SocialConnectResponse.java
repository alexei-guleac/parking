package com.isd.parking.web.rest.payload.account.connect;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.isd.parking.web.rest.payload.ActionSuccessResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;


@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@ApiModel(description = "Social provider profile connect response. ")
public class SocialConnectResponse extends ActionSuccessResponse {

    @JsonProperty()
    @JsonAlias({"connect"})
    @ApiModelProperty(notes = "Operation type. ", required = true)
    private boolean connect = true;

    public SocialConnectResponse(@NotBlank @NonNull boolean success) {
        super(success);
    }

}

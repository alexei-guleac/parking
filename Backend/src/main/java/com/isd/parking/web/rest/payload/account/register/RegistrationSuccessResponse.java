package com.isd.parking.web.rest.payload.account.register;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.isd.parking.web.rest.payload.ActionSuccessResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;


@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "User registration server response. ")
public class RegistrationSuccessResponse extends ActionSuccessResponse {

    @JsonProperty()
    @JsonAlias({"confirmationSent"})
    @ApiModelProperty(notes = "Confirms that email confirmation sent. ")
    @NotBlank
    @NonNull
    private boolean confirmationSent;

    public RegistrationSuccessResponse(@NotBlank @NonNull boolean success,
                                       @NotBlank @NonNull boolean confirmationSent) {
        super(success);
        this.confirmationSent = confirmationSent;
    }

}

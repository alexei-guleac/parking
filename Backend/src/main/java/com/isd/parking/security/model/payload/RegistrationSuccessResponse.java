package com.isd.parking.security.model.payload;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;


@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationSuccessResponse extends ActionSuccessResponse {

    @JsonProperty()
    @JsonAlias({"confirmationSent"})
    @NotBlank
    @NonNull
    private boolean confirmationSent;

    public RegistrationSuccessResponse(@NotBlank @NonNull boolean success, @NotBlank @NonNull boolean confirmationSent) {
        super(success);
        this.confirmationSent = confirmationSent;
    }

}

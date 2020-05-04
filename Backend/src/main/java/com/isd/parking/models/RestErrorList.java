package com.isd.parking.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;

import static java.util.Arrays.asList;


@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class RestErrorList extends ArrayList<ErrorMessage> {

    private HttpStatus status;

    public RestErrorList(@NotNull HttpStatus status, ErrorMessage... errors) {
        this(status.value(), errors);
    }

    public RestErrorList(int status, ErrorMessage... errors) {
        super();
        this.status = HttpStatus.valueOf(status);
        addAll(asList(errors));
    }

}

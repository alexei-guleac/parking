package com.isd.parking.models.users;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@Data
public final class SocialUser extends User {

    /* Social id's*/

    @JsonProperty()
    @JsonAlias({"id"})
    String id;
}


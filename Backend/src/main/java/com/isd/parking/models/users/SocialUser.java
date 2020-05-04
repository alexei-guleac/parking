package com.isd.parking.models.users;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;


/**
 * Social user model
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "Social user model. ")
public final class SocialUser extends User {

    /* Social id's*/
    @JsonProperty()
    @JsonAlias({"social_ids"})
    @ApiModelProperty(notes = "Map of user social id's by social service providers")
    private Map<String, String> socialIds;

}


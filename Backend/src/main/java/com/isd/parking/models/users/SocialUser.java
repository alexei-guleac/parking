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

    /*@JsonProperty()
    @JsonAlias({"fbid"})
    private @Attribute(name = "fbid")
    String fbid;

    @JsonProperty()
    @JsonAlias({"gid"})
    private @Attribute(name = "gid")
    String gid;

    @JsonProperty()
    @JsonAlias({"twid"})
    private @Attribute(name = "twid")
    String twid;

    @JsonProperty()
    @JsonAlias({"msid"})
    private @Attribute(name = "msid")
    String msid;

    @JsonProperty()
    @JsonAlias({"gitid"})
    private @Attribute(name = "gitid")
    String gitid;

    @JsonProperty()
    @JsonAlias({"instid"})
    private @Attribute(name = "instid")
    String instid;

    @JsonProperty()
    @JsonAlias({"aid"})
    private @Attribute(name = "aid")
    String aid;

    @JsonProperty()
    @JsonAlias({"vkid"})
    private @Attribute(name = "vkid")
    String vkid;

    @JsonProperty()
    @JsonAlias({"linkid"})
    private @Attribute(name = "linkid")
    String linkid;*/

}


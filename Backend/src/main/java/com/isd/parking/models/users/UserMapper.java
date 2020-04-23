package com.isd.parking.models.users;

import org.mapstruct.*;
import org.springframework.stereotype.Component;


/**
 * Provides User.class and UserLdap.class bidirectional conversing
 */
@Component
@Mapper(componentModel = "spring",
    nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface UserMapper {

    /**
     * Convert User to UserLdap model
     *
     * @param user - target user fro mapping
     * @return LDAP user model from specified user
     */
    @Mappings({
        @Mapping(target = "uid", source = "user.username"),
        @Mapping(target = "cn", expression = "java(user.getFullname())"),
        @Mapping(target = "sn", source = "user.lastname"),
        @Mapping(target = "userPassword", source = "user.password")
    })
    UserLdap userToUserLdap(User user);

    /**
     * Convert UserLdap to User model
     *
     * @param userLdap - target LDAP user fro mapping
     * @return user model from specified LDAP user
     */
    @Mappings({
        @Mapping(target = "username", source = "userLdap.uid"),
        @Mapping(target = "firstname", expression = "java(userLdap.getCn().split(\" \")[0])"),
        @Mapping(target = "lastname", source = "userLdap.sn"),
        @Mapping(target = "password", source = "userLdap.userPassword")
    })
    User userLdapToUser(UserLdap userLdap);
}


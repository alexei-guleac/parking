package com.isd.parking.models.users;

import org.mapstruct.*;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;


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
     * LDAP User name mapping
     *
     * @param userLdap - target user
     * @return - user firstname
     */
    @Named("getFirstname")
    static String getFirstname(UserLdap userLdap) {
        if (userLdap.getCn() != null) {
            return userLdap.getCn().split(" ")[0];
        } else {
            return null;
        }
    }

    /**
     * User social id's mapping
     *
     * @param sourceMap - source map
     * @return map with removed unnecessary LDAP string content
     */
    @Named("socialIdsSplit")
    static Map<String, String> socialIdsSplit(Map<String, String> sourceMap) {

        return sourceMap.entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> entry.getValue().split(":")[1].strip()));
    }

    /**
     * Convert User to UserLdap model
     *
     * @param user - target user for mapping
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
     * @param userLdap - target LDAP user for mapping
     * @return user model from specified LDAP user
     */
    @Mappings({
        @Mapping(target = "username", source = "userLdap.uid"),
        @Mapping(target = "firstname", expression = "java(userLdap.getCn().split(\" \")[0])"),
        @Mapping(target = "lastname", source = "userLdap.sn"),
        @Mapping(target = "password", source = "userLdap.userPassword")
    })
    User userLdapToUser(UserLdap userLdap);

    /**
     * Convert UserLdap to SocialUser model
     *
     * @param userLdap - target LDAP user for mapping
     * @return social user model from specified LDAP user
     */
    @Mappings({
        @Mapping(target = "username", source = "userLdap.uid"),
        @Mapping(target = "firstname",
            qualifiedByName = "getFirstname"),
        @Mapping(target = "lastname", source = "userLdap.sn"),
        @Mapping(target = "password", source = "userLdap.userPassword"),
        @Mapping(target = "socialIds",
            source = "userLdap.socialIds", qualifiedByName = "socialIdsSplit")
    })
    SocialUser userLdapToSocialUser(UserLdap userLdap);
}

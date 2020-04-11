package com.isd.parking.models.users;

import org.mapstruct.*;
import org.springframework.stereotype.Component;


@Component
@Mapper(componentModel = "spring",
    nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface UserMapper {

    @Mappings({
        @Mapping(target = "uid", source = "user.username"),
        @Mapping(target = "cn", expression = "java(user.getFullname())"),
        @Mapping(target = "sn", source = "user.lastname"),
        @Mapping(target = "userPassword", source = "user.password")
    })
    UserLdap userToUserLdap(User user);

    @Mappings({
        @Mapping(target = "username", source = "userLdap.uid"),
        @Mapping(target = "firstname", expression = "java(userLdap.getCn().split(\" \")[0])"),
        @Mapping(target = "lastname", source = "userLdap.sn"),
        @Mapping(target = "password", source = "userLdap.userPassword")
    })
    User userLdapToUser(UserLdap userLdap);
}


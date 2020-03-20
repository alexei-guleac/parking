package com.isd.parking.models;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.stereotype.Component;


@Component
@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mappings({
        @Mapping(target = "uid", source = "user.username"),
        @Mapping(target = "cn", source = "user.fullname"),
        @Mapping(target = "sn", source = "user.lastname"),
        @Mapping(target = "userPassword", source = "user.password")
    })
    UserLdap userToUserLdap(User user);

    @Mappings({
        @Mapping(target = "username", source = "userLdap.uid"),
        @Mapping(target = "fullname", source = "userLdap.cn"),
        @Mapping(target = "lastname", source = "userLdap.sn"),
        @Mapping(target = "password", source = "userLdap.userPassword")
    })
    User userLdapToUser(UserLdap userLdap);
}


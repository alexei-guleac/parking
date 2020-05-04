package com.isd.parking.repository;

import com.isd.parking.models.users.UserLdap;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.ldap.repository.LdapRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * User ldap repository
 */
@Repository
public interface UserRepository extends LdapRepository<UserLdap> {

    @NotNull UserLdap findByUid(String username);

    @NotNull UserLdap findByUidAndUserPassword(String username, String password);

    @NotNull List<UserLdap> findByUidLikeIgnoreCase(String username);

    @NotNull List<UserLdap> findAll();
}

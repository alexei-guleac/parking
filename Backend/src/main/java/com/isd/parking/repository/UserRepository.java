package com.isd.parking.repository;

import com.isd.parking.models.users.UserLdap;
import org.springframework.data.ldap.repository.LdapRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * User ldap repository
 */
@Repository
public interface UserRepository extends LdapRepository<UserLdap> {

    UserLdap findByUid(String username);

    UserLdap findByUidAndUserPassword(String username, String password);

    List<UserLdap> findByUidLikeIgnoreCase(String username);

    List<UserLdap> findAll();
}

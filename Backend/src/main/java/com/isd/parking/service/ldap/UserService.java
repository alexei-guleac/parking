package com.isd.parking.service.ldap;

import com.isd.parking.models.users.UserLdap;
import com.isd.parking.repository.UserRepository;
import com.isd.parking.security.PasswordEncoding;
import com.isd.parking.utils.ColorConsoleOutput;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    private final ColorConsoleOutput console;

    public UserService(UserRepository userRepository, ColorConsoleOutput console) {
        this.userRepository = userRepository;
        this.console = console;
    }

    // --------- LDAp repository methods ---------

    public List<String> search(final String username) {
        log.info(console.methodMsg(""));
        List<UserLdap> userList = userRepository.findByUidLikeIgnoreCase(username);
        if (userList == null) {
            return Collections.emptyList();
        }
        //return userList;
        return userList.stream()
            .map(UserLdap::getCn)
                .collect(Collectors.toList());
    }

    /**
     * Method update user with given username and password
     *
     * @param username - user name
     * @param password - user pass
     */
    public void modify(final String username, final String password) {
        UserLdap user = userRepository.findByUid(username);
        user.setUserPassword(password);

        userRepository.save(user);
    }

    //not working

    /**
     * Method create user with given username and password
     *
     * @param username - user name
     * @param password - user pass
     */
    public void create(final String username, final String password) {
        log.info(console.methodMsg(""));
        UserLdap newUser = new UserLdap(username, PasswordEncoding.CustomBcryptPasswordEncoder.digestSHA(password));
        newUser.setId(LdapUtils.emptyLdapName());
        //newUser.setId(LdapUtils.newLdapName(new DistinguishedName("uid=" + username + ",ou=people")));

        userRepository.save(newUser);
    }

    public List<UserLdap> findAll() {
        log.info(console.methodMsg(""));
        return userRepository.findAll();
    }
}

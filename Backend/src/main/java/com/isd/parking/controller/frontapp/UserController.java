package com.isd.parking.controller.frontapp;

import com.isd.parking.config.security.CustomPasswordEncoder;
import com.isd.parking.model.User;
import com.isd.parking.service.ldap.UserLdapClient;
import com.isd.parking.utils.ColorConsoleOutput;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.isd.parking.controller.frontapp.RestApiEndpoints.users;
import static com.isd.parking.utils.ColorConsoleOutput.*;


/**
 * User controller
 * Provides methods for login, registration of user,
 * reservation and cancel reservation parking lot by user
 */
@RestController
@CrossOrigin(origins = "*")
@Slf4j
public class UserController {

    private final UserLdapClient userLdapClient;

    private final ColorConsoleOutput console;

    @Value("${spring.ldap.base}")
    private String ldapSearchBase;

    @Autowired
    public UserController(UserLdapClient userLdapClient, ColorConsoleOutput console) {
        this.userLdapClient = userLdapClient;
        this.console = console;
    }

    /**
     * Users login controller
     * Used to authentificate user and login in system
     *
     * @return - success status of provided login
     */
    @RequestMapping("/login")
    public boolean login(@RequestBody User user) {
        final String username = user.getUsername();
        final String password = user.getPassword();
        //log.info(console.classMsg("LDAP enabled: ") + Boolean.parseBoolean(ldapEnabled));
        //String test = userService.getUserDetail(username);
        //log.info(console.classMsg("Get: ") + test);

        // LDAP
        log.info(console.classMsg(getClass().getSimpleName()," Request body: ") + blTxt(String.valueOf(user)));
        log.info(console.classMsg(getClass().getSimpleName()," login request body: ") + blTxt(username + " " + password));
        //log.info(String.valueOf(userService.authenticate(username, password)));
        return userLdapClient.authenticate(username, password);
    }

    /**
     * Users registration controller
     * Handles user registration in system
     *
     * @return - success status of provided registration
     */
    @RequestMapping("/registration")
    public boolean registration(@RequestBody User user) {
        log.info(console.classMsg(getClass().getSimpleName(),"request body: ") + user);
        final String username = user.getUsername();
        final String password = user.getPassword();
        log.info(console.classMsg(getClass().getSimpleName(),"registration request body: ") + blTxt(username + " " + password));

        //verify if user exists in db and throw error, else create
        List<String> sameUserNames = userLdapClient.searchUser(username);
        log.info(String.valueOf(sameUserNames));

        if (sameUserNames.isEmpty()) {
            userLdapClient.createUser(new User(user, new CustomPasswordEncoder(console).encode(password)));

            sameUserNames = userLdapClient.searchUser(username);
            log.info(String.valueOf(sameUserNames));

            User createdUser = userLdapClient.findByDn("uid=" + username + "," + ldapSearchBase);
            if (createdUser == null) {
                log.info(console.classMsg(getClass().getSimpleName()," User not created: ") + blTxt(String.valueOf(createdUser)));
                return false;
            } else {
                log.info(console.classMsg(getClass().getSimpleName()," Created user found: ") + blTxt(String.valueOf(createdUser)));
                return true;
            }
        } else {
            return false;
        }
    }

    @ResponseBody
    @GetMapping("/" + users)
    public Iterable<User> getAllUsers() {
        return userLdapClient.findAll();
    }
}

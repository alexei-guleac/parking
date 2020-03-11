package com.isd.parking.controller.web;

import com.isd.parking.model.User;
import com.isd.parking.security.CustomPasswordEncoder;
import com.isd.parking.service.RestService;
import com.isd.parking.service.ldap.UserLdapClient;
import com.isd.parking.utils.ColorConsoleOutput;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import static com.isd.parking.controller.web.RestApiEndpoints.users;
import static com.isd.parking.utils.ColorConsoleOutput.blTxt;


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

    private final RestService restService;

    private final ColorConsoleOutput console;

    @Value("${spring.ldap.base}")
    private String ldapSearchBase;

    @Autowired
    public UserController(UserLdapClient userLdapClient, RestService restService, ColorConsoleOutput console) {
        this.userLdapClient = userLdapClient;
        this.restService = restService;
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
        log.info(console.classMsg(getClass().getSimpleName(), " Request body: ") + blTxt(String.valueOf(user)));
        log.info(console.classMsg(getClass().getSimpleName(), " login request body: ") + blTxt(username + " " + password));
        //log.info(String.valueOf(userService.authenticate(username, password)));
        return userLdapClient.authenticate(username, password);
    }

    @RequestMapping("/login/fb")
    public boolean loginFb(@RequestBody String id) {
        //log.info(console.classMsg("LDAP enabled: ") + Boolean.parseBoolean(ldapEnabled));
        //String test = userService.getUserDetail(username);
        log.info(console.classMsg(getClass().getSimpleName(), "fb login " + id));

        return userLdapClient.searchUserBySocialId(new JSONObject(id).getString("id"), "fb");
    }

    /**
     * Users registration controller
     * Handles user registration in system
     *
     * @return - success status of provided registration
     */
    @RequestMapping("/registration")
    public boolean registration(@RequestBody User user) {
        log.info(console.classMsg(getClass().getSimpleName(), "request body: ") + user);
        final String username = user.getUsername();
        final String password = user.getPassword();
        log.info(console.classMsg(getClass().getSimpleName(), "registration request body: ") + blTxt(username + " " + password));

        //verify if user exists in db and throw error, else create
        if (!userLdapClient.searchUser(username)) {
            userLdapClient.createUser(new User(user, new CustomPasswordEncoder(console).encode(password)));

            boolean exists = userLdapClient.searchUser(username);
            log.info(String.valueOf(exists));
            User createdUser = userLdapClient.findByDn("uid=" + username + "," + ldapSearchBase);
            if (createdUser == null) {
                log.info(console.classMsg(getClass().getSimpleName(), " User not created: ") + blTxt(String.valueOf(createdUser)));
                return false;
            } else {
                log.info(console.classMsg(getClass().getSimpleName(), " Created user found: ") + blTxt(String.valueOf(createdUser)));
                return true;
            }
        } else {
            return false;
        }
    }

    @ResponseBody
    @RequestMapping("/validate_captcha")
    public String grecaptcha(@RequestBody String grecaptcha) {
        log.info(console.classMsg(getClass().getSimpleName(), " Request body: ") + blTxt(grecaptcha));
        String grecaptchaToken = new JSONObject(grecaptcha).getJSONObject("grecaptcha").getString("token");
        log.info(grecaptchaToken);

        if (grecaptchaToken == null || grecaptchaToken.equals("")) {
            return "{\"success\": false, \"message\": \"Token is empty or invalid\"}";
        } else {

            String secretKey = "6Lcngd8UAAAAAGP04a5nTLgeZKXDWKasd-A1rHHQ"; //the secret key from your google admin console;

            //token validation url is URL: https://www.google.com/recaptcha/api/siteverify
            // METHOD used is: POST

            String url = String.format("https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s", secretKey, grecaptchaToken);
            String response = restService.getPlainJSON(url);
            log.info(response);

            //sample message
            /*{
                "success": true,
                "challenge_ts": "2020-03-07T13:40:25Z",
                "hostname": "localhost"
            }*/

            return response;
        }
    }

    @ResponseBody
    @GetMapping("/" + users)
    public Iterable<User> getAllUsers() {
        return userLdapClient.findAll();
    }
}

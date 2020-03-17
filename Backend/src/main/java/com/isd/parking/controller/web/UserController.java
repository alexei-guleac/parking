package com.isd.parking.controller.web;

import com.isd.parking.models.User;
import com.isd.parking.security.PasswordEncoding;
import com.isd.parking.security.model.ConfirmationToken;
import com.isd.parking.security.model.payload.ActionSuccessResponse;
import com.isd.parking.security.model.payload.GRecaptcha;
import com.isd.parking.security.model.payload.RecaptchaResponse;
import com.isd.parking.security.model.payload.ResetPasswordRequest;
import com.isd.parking.service.ConfirmationTokenService;
import com.isd.parking.service.EmailSenderService;
import com.isd.parking.service.RestService;
import com.isd.parking.service.ldap.UserLdapClient;
import com.isd.parking.utils.ColorConsoleOutput;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;

import static com.isd.parking.controller.web.RestApiEndpoints.users;
import static com.isd.parking.utils.ColorConsoleOutput.blTxt;
import static org.springframework.web.bind.annotation.RequestMethod.POST;


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

    private final ConfirmationTokenService confirmationTokenService;

    private final EmailSenderService emailSenderService;

    private final ColorConsoleOutput console;

    @Value("${spring.ldap.base}")
    private String ldapSearchBase;

    @Value("${spring.mail.from.email}")
    private String from;

    @Autowired
    public UserController(UserLdapClient userLdapClient, RestService restService, ConfirmationTokenService confirmationTokenService, EmailSenderService emailSenderService, ColorConsoleOutput console) {
        this.userLdapClient = userLdapClient;
        this.restService = restService;
        this.confirmationTokenService = confirmationTokenService;
        this.emailSenderService = emailSenderService;
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

    /**
     * Users registration controller
     * Handles user registration in system
     *
     * @return - success status of provided registration
     */
    @RequestMapping("/registration")
    public ResponseEntity<?> registration(@RequestBody User user) {
        log.info(console.classMsg(getClass().getSimpleName(), "request body: ") + user);
        final String username = user.getUsername();
        final String password = user.getPassword();
        log.info(console.classMsg(getClass().getSimpleName(), "registration request body: ") + blTxt(username + " " + password));

        //verify if user exists in db and throw error, else create
        boolean emailExists = userLdapClient.searchUsersByEmail(user.getEmail());
        boolean userExists = userLdapClient.searchUser(username);

        if (userExists) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Account with this username already exists");
        } else if (emailExists) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Account with this email already exists");
        } else {
            userLdapClient.createUser(new User(user, new PasswordEncoding.CustomPasswordEncoder(console).encode(password)));

            boolean exists = userLdapClient.searchUser(username);
            log.info(String.valueOf(exists));

            User createdUser = userLdapClient.findByDn("uid=" + username + "," + ldapSearchBase);
            if (createdUser == null) {
                log.info(console.classMsg(getClass().getSimpleName(), " User not created: ") + blTxt(String.valueOf(createdUser)));
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("User was not created");
            } else {
                // Create token
                ConfirmationToken confirmationToken = new ConfirmationToken(username);
                // Save it
                confirmationTokenService.save(confirmationToken);
                // Send email
                emailSenderService.sendRegistrationConfirmMail(user, confirmationToken);

                log.info(console.classMsg(getClass().getSimpleName(), " Created user found: ") + blTxt(String.valueOf(createdUser)));
                return ResponseEntity.ok(new ActionSuccessResponse(true));
            }
        }
    }

    // Endpoint to confirm the token
    @RequestMapping(value = "/confirm_account", method = POST)
    public ResponseEntity<?> confirmUserAccount(@RequestBody String body) {
        log.info("body " + body);
        final String confirmationToken = new JSONObject(body).getString("confirmationToken");
        Optional<ConfirmationToken> optionalConfirmationToken = confirmationTokenService.findByConfirmationToken(confirmationToken);
        log.info("token " + optionalConfirmationToken);

        if (optionalConfirmationToken.isPresent()) {
            ConfirmationToken token = optionalConfirmationToken.get();

            if (confirmationTokenService.assertNotExpired(token)) {

                User user = userLdapClient.findByDn("uid=" + optionalConfirmationToken.get().getUid() + "," + ldapSearchBase);
                log.info(console.classMsg(getClass().getSimpleName(), " Created user found: ") + blTxt(String.valueOf(user)));
                //user.setEnabled(true);
                return ResponseEntity.ok(new ActionSuccessResponse(true));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Confirmation token is expired. Register again");
            }
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Confirmation token not found. Register again");
        }
    }

    @ResponseBody
    @RequestMapping("/validate_captcha")
    public ResponseEntity grecaptcha(@RequestBody String grecaptcha) {
        final String grecaptchaToken = new JSONObject(grecaptcha).getJSONObject("grecaptcha").getString("token");

        if (grecaptchaToken == null || grecaptchaToken.equals("")) {

            return ResponseEntity.ok(RecaptchaResponse.builder()
                    .success(false)
                    .challenge_ts(new Date(System.currentTimeMillis()))
                    .hostname("localhost")
                    .message("Token is empty or invalid").build());
        } else {
            String secretKey = GRecaptcha.SECRET_KEY;
            String url = String.format(GRecaptcha.GRECAPTCHA_API_URL, secretKey, grecaptchaToken);

            String response = restService.getPlainJSON(url);
            log.info(response);

            //sample message
            /*{
                "success": true,
                "challenge_ts": "2020-03-07T13:40:25Z",
                "hostname": "localhost"
            }*/

            return ResponseEntity.ok(response);
        }
    }

    // Receive the address and send an email
    @RequestMapping(value = "/forgot-password", method = POST)
    public ResponseEntity<?> forgotUserPassword(@RequestBody String email) {

        final String userEmail = new JSONObject(email).getString("email");
        User existingUser = userLdapClient.getUserByEmail(userEmail);

        if (existingUser != null) {
            log.info(console.classMsg(getClass().getSimpleName(), " User exists: ") + blTxt(String.valueOf(existingUser)));

            Optional<ConfirmationToken> lastConfirmationToken = confirmationTokenService.findLastByUsername(existingUser.getUsername());
            log.info("past token " + lastConfirmationToken);

            if (lastConfirmationToken.isPresent()) {
                ConfirmationToken token = lastConfirmationToken.get();

                if (!confirmationTokenService.assertValidForRepeat(token)) {
                    log.info(console.classMsg(getClass().getSimpleName(), " Password reset not allowed: ") + blTxt(String.valueOf(existingUser)));
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("Password reset is allowed only once a month.");
                }
            }
            // Create token
            ConfirmationToken confirmationToken = new ConfirmationToken(existingUser.getUsername());
            // Save it
            confirmationTokenService.save(confirmationToken);
            // Send email
            emailSenderService.sendPassResetMail(existingUser, confirmationToken);

            return ResponseEntity.ok(new ActionSuccessResponse(true));

        } else {
            log.info(console.classMsg(getClass().getSimpleName(), " User not exists: ") + blTxt(String.valueOf(existingUser)));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("User with this email doesn't exists on the server");
        }
    }

    // Endpoint to update a user's password
    @RequestMapping(value = "/reset-password", method = POST)
    public ResponseEntity<?> resetUserPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {

        final String username = resetPasswordRequest.getUsername();
        final String password = resetPasswordRequest.getPassword();

        if (username != null) {
            // Use username to find user
            boolean userExists = userLdapClient.searchUser(username);
            log.info(String.valueOf(userExists));

            if (userExists) {
                userLdapClient.updateUserPassword(username, new PasswordEncoding.CustomPasswordEncoder(console).encode(password));

                return ResponseEntity.ok(new ActionSuccessResponse(true));
            } else {
                log.info(console.classMsg(getClass().getSimpleName(), " User not exists: ") + blTxt(username));
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("User with this username doesn't exists on the server");
            }
        } else {
            log.info(console.classMsg(getClass().getSimpleName(), " Username empty: ") + blTxt(String.valueOf(username)));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Reset request with username empty parameter");
        }
    }

    @ResponseBody
    @GetMapping("/" + users)
    public Iterable<User> getAllUsers() {
        return userLdapClient.findAll();
    }
}

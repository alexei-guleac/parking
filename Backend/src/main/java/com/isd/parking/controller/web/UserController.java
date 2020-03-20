package com.isd.parking.controller.web;

import com.isd.parking.models.User;
import com.isd.parking.models.UserLdap;
import com.isd.parking.models.UserMapper;
import com.isd.parking.security.AccountConfirmationPeriods;
import com.isd.parking.security.PasswordEncoding.CustomBcryptPasswordEncoder;
import com.isd.parking.security.model.AcoountOperation;
import com.isd.parking.security.model.ConfirmationToken;
import com.isd.parking.security.model.payload.ActionSuccessResponse;
import com.isd.parking.security.model.payload.RecaptchaResponse;
import com.isd.parking.security.model.payload.ResetPasswordRequest;
import com.isd.parking.service.ConfirmationTokenService;
import com.isd.parking.service.EmailSenderService;
import com.isd.parking.service.RestService;
import com.isd.parking.service.ldap.UserLdapClient;
import com.isd.parking.utils.AppDateUtils;
import com.isd.parking.utils.ColorConsoleOutput;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

import static com.isd.parking.controller.web.RestApiEndpoints.users;
import static com.isd.parking.utils.AppDateUtils.isBeforeNow;
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

    private final UserMapper userMapper;

    @Autowired
    public UserController(UserLdapClient userLdapClient, RestService restService, ConfirmationTokenService confirmationTokenService, EmailSenderService emailSenderService, ColorConsoleOutput console, UserMapper userMapper) {
        this.userLdapClient = userLdapClient;
        this.restService = restService;
        this.confirmationTokenService = confirmationTokenService;
        this.emailSenderService = emailSenderService;
        this.console = console;
        this.userMapper = userMapper;
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

        // log.info(console.classMsg("LDAP enabled: ") + Boolean.parseBoolean(ldapEnabled));
        // String test = userService.getUserDetail(username);
        // log.info(console.classMsg("Get: ") + test);

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
        final String email = user.getEmail();
        log.info(console.classMsg(getClass().getSimpleName(), "registration request body: ") + blTxt(username + " " + password));

        //verify if user exists in db and throw error, else create
        boolean userExists = userLdapClient.searchUser(username);
        boolean emailExists = userLdapClient.searchUsersByEmail(email);

        if (userExists) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Account with this username already exists");
        } else if (emailExists) {
            UserLdap existedUser = userLdapClient.getUserByEmail(email);

            log.info(console.methodMsg("accountConfirmationIsExpired " + existedUser.accountConfirmationIsExpired()));
            log.info(console.methodMsg("accountConfirmationValid " + existedUser.accountConfirmationValid()));

            if (existedUser.accountConfirmationIsExpired()) {
                log.info("from mapped user " + user);
                UserLdap newUser = userMapper.userToUserLdap(user);
                log.info("mapped user " + newUser);

                userLdapClient.deleteUser(newUser);
                return createUserResult(newUser);
            }
            if (existedUser.accountConfirmationValid()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Account with this email already exists and waiting for confirmation");
            }

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Account with this email already exists");

        } else {
            log.info("from mapped user " + user);
            UserLdap newUser = userMapper.userToUserLdap(user);
            log.info("mapped user " + newUser);

            return createUserResult(newUser);
        }
    }

    private ResponseEntity<?> createUserResult(UserLdap newUser) {
        userLdapClient.createUser(newUser);

        boolean exists = userLdapClient.searchUser(newUser.getUid());
        log.info("exists" + exists);

        UserLdap createdUser = userLdapClient.findById(newUser.getUid());
        if (createdUser == null) {
            log.info(console.classMsg(getClass().getSimpleName(), " User not created: ") + blTxt(String.valueOf(createdUser)));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("User was not created");
        } else {
            createConfirmation(createdUser);
            log.info(console.classMsg(getClass().getSimpleName(), " Created user found: ") + blTxt(String.valueOf(createdUser)));
            return ResponseEntity.ok(new ActionSuccessResponse(true));
        }
    }

    private void createConfirmation(UserLdap createdUser) {
        // Create token
        ConfirmationToken confirmationToken = new ConfirmationToken(createdUser.getUid(), AcoountOperation.ACCOUNT_CONFIRMATION);
        // Save it
        confirmationTokenService.save(confirmationToken);
        // Send email
        emailSenderService.sendRegistrationConfirmMail(createdUser, confirmationToken);
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
                UserLdap user = userLdapClient.findById(optionalConfirmationToken.get().getUid());
                log.info(console.classMsg(getClass().getSimpleName(), " Created user found: ") + blTxt(String.valueOf(user)));

                if (token.getOperationType() == AcoountOperation.ACCOUNT_CONFIRMATION) {
                    user.setAccountEnabled();
                    userLdapClient.updateUser(user);
                }

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
            String secretKey = RecaptchaResponse.GRecaptcha.SECRET_KEY;
            String url = String.format(RecaptchaResponse.GRecaptcha.GRECAPTCHA_API_URL, secretKey, grecaptchaToken);

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
        UserLdap existingUser = userLdapClient.getUserByEmail(userEmail);

        if (existingUser != null) {
            log.info(console.classMsg(getClass().getSimpleName(), " User exists: ") + blTxt(String.valueOf(existingUser)));

            Optional<ConfirmationToken> lastConfirmationToken = confirmationTokenService.findLastByUsername(existingUser.getUid());
            log.info("past token " + lastConfirmationToken);

            if (lastConfirmationToken.isPresent()) {
                ConfirmationToken token = lastConfirmationToken.get();

                if (!confirmationTokenService.assertValidForRepeat(token)) {
                    log.info(console.classMsg(getClass().getSimpleName(), " Password reset not allowed: ") + blTxt(String.valueOf(existingUser)));
                    String periodEnding = AppDateUtils.getPeriodEnding(AccountConfirmationPeriods.REQUEST_CONFIRMATION_TOKEN_PERIOD_IN_DAYS);
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Password reset request is allowed only " + periodEnding);
                }
            }
            // Create token
            ConfirmationToken confirmationToken = new ConfirmationToken(existingUser.getUid(), AcoountOperation.PASSWORD_RESET);
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

        final String confirmationToken = resetPasswordRequest.getConfirmationToken();
        final String password = resetPasswordRequest.getPassword();

        Optional<ConfirmationToken> optionalConfirmationToken = confirmationTokenService.findByConfirmationToken(confirmationToken);
        log.info("token reset-password" + optionalConfirmationToken);

        if (optionalConfirmationToken.isPresent()) {
            ConfirmationToken token = optionalConfirmationToken.get();

            if (confirmationTokenService.assertNotExpired(token)) {
                final String username = token.getUid();
                if (username != null) {
                    // Use username to find user
                    UserLdap user = userLdapClient.findById(username);
                    log.info(String.valueOf(user));

                    if (user != null) {
                        LocalDateTime passwordUpdatedAt = userLdapClient.getPasswordUpdateAt(user.getUid());
                        if (assertPassValidForChange(passwordUpdatedAt)) {

                            userLdapClient.updateUserPassword(user, new CustomBcryptPasswordEncoder().encode(password));
                            return ResponseEntity.ok(new ActionSuccessResponse(true));
                        } else {
                            log.info(console.classMsg(getClass().getSimpleName(), " Password reset not allowed: ") + blTxt(String.valueOf(user)));
                            String periodEnding = AppDateUtils.getPeriodEnding(AccountConfirmationPeriods.RESET_PASSWORD_PERIOD_IN_DAYS);
                            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Password change is allowed only " + periodEnding);
                        }
                    } else {
                        log.info(console.classMsg(getClass().getSimpleName(), " User not exists: ") + blTxt(username));
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("User with this username doesn't exists on the server");
                    }
                } else {
                    log.info(console.classMsg(getClass().getSimpleName(), " Username empty: ") + blTxt(String.valueOf(username)));
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Confirmation token with username empty parameter");
                }
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Confirmation token is expired. Request again");
            }
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Confirmation token not found. Request again");
        }
    }

    private boolean assertPassValidForChange(LocalDateTime passwordUpdatedAt) {
        log.info("expires " + passwordUpdatedAt.plusDays(AccountConfirmationPeriods.RESET_PASSWORD_PERIOD_IN_DAYS));
        return isBeforeNow(passwordUpdatedAt.plusDays(AccountConfirmationPeriods.RESET_PASSWORD_PERIOD_IN_DAYS), AccountConfirmationPeriods.MAX_CLOCK_SKEW_MINUTES);
    }

    @ResponseBody
    @GetMapping("/" + users)
    public Iterable<UserLdap> getAllUsers() {
        return userLdapClient.findAll();
    }
}

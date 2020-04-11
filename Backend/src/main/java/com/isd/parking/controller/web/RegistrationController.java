package com.isd.parking.controller.web;

import com.isd.parking.models.users.User;
import com.isd.parking.models.users.UserLdap;
import com.isd.parking.models.users.UserMapper;
import com.isd.parking.security.model.AccountOperation;
import com.isd.parking.security.model.ConfirmationToken;
import com.isd.parking.security.model.payload.RegistrationSuccessResponse;
import com.isd.parking.security.model.payload.register.DeviceInfo;
import com.isd.parking.security.model.payload.register.RegistrationRequest;
import com.isd.parking.security.model.payload.register.SocialRegisterRequest;
import com.isd.parking.service.implementations.ConfirmationTokenServiceImpl;
import com.isd.parking.service.implementations.EmailSenderServiceImpl;
import com.isd.parking.service.ldap.UserLdapClient;
import com.isd.parking.utils.ColorConsoleOutput;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.io.IOException;

import static com.isd.parking.controller.ApiEndpoints.register;
import static com.isd.parking.controller.ApiEndpoints.social;
import static com.isd.parking.utils.AppStringUtils.generateCommonLangPassword;
import static com.isd.parking.utils.ColorConsoleOutput.blTxt;
import static com.isd.parking.utils.ColorConsoleOutput.methodMsgStatic;
import static org.springframework.web.bind.annotation.RequestMethod.POST;


@RestController
@RequestMapping(register)
@Slf4j
public class RegistrationController {

    private final UserLdapClient userLdapClient;

    private final ConfirmationTokenServiceImpl confirmationTokenService;

    private final EmailSenderServiceImpl emailSenderService;

    private final ColorConsoleOutput console;

    private final UserMapper userMapper;

    @Value("${spring.ldap.base}")
    private String ldapSearchBase;

    @Autowired
    public RegistrationController(UserLdapClient userLdapClient,
                                  ConfirmationTokenServiceImpl confirmationTokenService,
                                  EmailSenderServiceImpl emailSenderService,
                                  ColorConsoleOutput console, UserMapper userMapper) {
        this.userLdapClient = userLdapClient;
        this.confirmationTokenService = confirmationTokenService;
        this.emailSenderService = emailSenderService;
        this.console = console;
        this.userMapper = userMapper;
    }


    /**
     * Users registration controller
     * Handles user registration in system
     *
     * @return - success status of provided registration
     */
    @RequestMapping(method = POST)
    public ResponseEntity<?> registration(@RequestBody RegistrationRequest request) {
        log.info(console.classMsg(getClass().getSimpleName(), "request body: ") + request);
        User user = request.getUser();
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
            final UserLdap existedUser = userLdapClient.getUserByEmail(email);

            log.info(console.methodMsg("accountConfirmationIsExpired " + existedUser.accountConfirmationIsExpired()));
            log.info(console.methodMsg("accountConfirmationValid " + existedUser.accountConfirmationValid()));

            if (existedUser.accountConfirmationIsExpired()) {
                log.info("from mapped user " + user);
                UserLdap newUser = userMapper.userToUserLdap(user);
                log.info("mapped user " + newUser);

                userLdapClient.deleteUser(newUser);
                return createUserResult(newUser, request.getDeviceInfo());
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

            return createUserResult(newUser, request.getDeviceInfo());
        }
    }

    @RequestMapping(social)
    public ResponseEntity<?> socialRegistration(@RequestBody SocialRegisterRequest request) {
        log.info(console.classMsg(getClass().getSimpleName(), "registration request body: ") + request);
        final User user = request.getUser();
        final String email = user.getEmail();
        final String id = request.getId();
        final String provider = request.getSocialProvider();
        log.info(methodMsgStatic(" social: " + provider));
        log.info(methodMsgStatic(" social id: " + id));

        //verify if user exists in db and throw error, else create
        final UserLdap userFound = userLdapClient.getUserBySocialId(id, provider);
        log.info(methodMsgStatic("userFound: " + userFound));
        if (userFound != null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Account with this social id already exists");
        } else {
            if (email != null) {
                log.info(methodMsgStatic("email: " + email));
                final UserLdap existedUser = userLdapClient.getUserByEmail(email);

                if (existedUser != null) {
                    log.info(console.methodMsg("accountConfirmationIsExpired " + existedUser.accountConfirmationIsExpired()));
                    log.info(console.methodMsg("accountConfirmationValid " + existedUser.accountConfirmationValid()));

                    if (existedUser.accountConfirmationIsExpired()) {
                        log.info("from mapped user " + user);
                        UserLdap newUser = userMapper.userToUserLdap(user);
                        log.info("mapped user " + newUser);
                        newUser.prepareSocialUser(id, provider);
                        newUser.setUserPassword(generateCommonLangPassword());

                        userLdapClient.deleteUser(newUser);
                        return createUserResult(newUser, request.getDeviceInfo());
                    }
                    if (existedUser.accountConfirmationValid()) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("Social account with this email already exists and waiting for confirmation");
                    }

                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Social account with this email already exists");
                }
            }

            log.info("from mapped user " + user);
            UserLdap newUser = userMapper.userToUserLdap(user);
            log.info("mapped user " + newUser);
            newUser.prepareSocialUser(id, provider);
            log.info("after prepareSocialUser user " + newUser);
            return createUserResult(newUser, request.getDeviceInfo());
        }
    }

    private ResponseEntity<?> createUserResult(UserLdap newUser, DeviceInfo deviceInfo) {
        userLdapClient.createUser(newUser);

        boolean exists = userLdapClient.searchUser(newUser.getUid());
        log.info("exists" + exists);

        UserLdap createdUser = userLdapClient.findById(newUser.getUid());
        if (createdUser == null) {
            log.info(console.classMsg(getClass().getSimpleName(), " User not created: ") + blTxt(String.valueOf(createdUser)));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("User was not created");
        } else {
            if (createdUser.getEmail() != null) {
                createConfirmation(createdUser, deviceInfo);
            }
            log.info(console.classMsg(getClass().getSimpleName(), " Created user found: ") + blTxt(String.valueOf(createdUser)));
            return ResponseEntity.ok(new RegistrationSuccessResponse(true, createdUser.getEmail() != null));
        }
    }

    private void createConfirmation(UserLdap createdUser, DeviceInfo deviceInfo) {
        // Create token
        ConfirmationToken confirmationToken = new ConfirmationToken(createdUser.getUid(), AccountOperation.ACCOUNT_CONFIRMATION);
        // Save it
        confirmationTokenService.save(confirmationToken);
        // Send email
        try {
            emailSenderService.sendRegistrationConfirmMail(createdUser, confirmationToken, deviceInfo);
        } catch (IOException | MessagingException e) {
            e.printStackTrace();
        }
    }
}


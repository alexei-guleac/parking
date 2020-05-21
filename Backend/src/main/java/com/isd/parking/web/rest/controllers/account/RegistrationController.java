package com.isd.parking.web.rest.controllers.account;

import com.isd.parking.models.AccountOperation;
import com.isd.parking.models.ConfirmationRecord;
import com.isd.parking.models.users.User;
import com.isd.parking.models.users.UserLdap;
import com.isd.parking.models.users.UserMapper;
import com.isd.parking.services.implementations.ConfirmationServiceImpl;
import com.isd.parking.services.implementations.EmailSenderServiceImpl;
import com.isd.parking.storage.ldap.UserServiceImpl;
import com.isd.parking.web.rest.ApiEndpoints;
import com.isd.parking.web.rest.payload.DeviceInfo;
import com.isd.parking.web.rest.payload.account.register.RegistrationRequest;
import com.isd.parking.web.rest.payload.account.register.RegistrationSuccessResponse;
import com.isd.parking.web.rest.payload.account.register.SocialRegisterRequest;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.io.IOException;

import static com.isd.parking.utilities.AppStringUtils.generateCommonLangPassword;
import static org.springframework.web.bind.annotation.RequestMethod.POST;


/**
 * Provides methods for
 * - user standard registration
 * - user registration using social service
 */
@RestController
@RequestMapping(ApiEndpoints.register)
@Slf4j
@Api(value = "Registration Controller",
    description = "Operations pertaining to user registration in system")
public class RegistrationController {

    private final UserServiceImpl userService;

    private final ConfirmationServiceImpl confirmationTokenService;

    private final EmailSenderServiceImpl emailSenderService;

    private final UserMapper userMapper;

    @Autowired
    public RegistrationController(UserServiceImpl userService,
                                  ConfirmationServiceImpl confirmationTokenService,
                                  EmailSenderServiceImpl emailSenderService,
                                  UserMapper userMapper) {
        this.userService = userService;
        this.confirmationTokenService = confirmationTokenService;
        this.emailSenderService = emailSenderService;
        this.userMapper = userMapper;
    }

    /**
     * Handles user registration in system
     *
     * @param request - registration request contains user information and device data for set email language
     * @return - HTTP response with registration error or success details
     */
    @ApiOperation(value = "${RegistrationController.registration.value}",
        response = ResponseEntity.class,
        notes = "${RegistrationController.registration.notes}")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Success|OK"),
        @ApiResponse(code = 500, message = "Account with this username already exists")
    })
    @ApiImplicitParams(
        @ApiImplicitParam(name = "request",
            value = "${RegistrationController.registration.registrationRequest}",
            required = true, dataType = "RegistrationRequest")
    )
    @RequestMapping(method = POST)
    public @NotNull ResponseEntity<?> registration(@RequestBody @NotNull RegistrationRequest request) {

        final User user = request.getUser();
        final String username = user.getUsername();
        final String email = user.getEmail();

        //verify if user exists in DB and throw error, else create
        boolean userExists = userService.searchUser(username);
        boolean emailExists = userService.searchUsersByEmail(email);

        if (userExists) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Account with this username already exists");
        } else if (emailExists) {
            final UserLdap existedUser = userService.getUserByEmail(email);

            // check account confirmation state
            // if user exists but account confirmation is expired (did not have time or forgot to confirm)
            // temporary unconfirmed user with this email will be deleted and new one created
            assert existedUser != null;
            if (existedUser.accountConfirmationIsExpired()) {
                final UserLdap newUser = userMapper.userToUserLdap(user);
                userService.deleteUser(newUser);

                return processUserCreation(newUser, request.getDeviceInfo());
            }
            // if user exists and account confirmation link is valid
            if (existedUser.accountConfirmationValid()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Account with this email already exists and waiting for confirmation");
            }

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Account with this email already exists");
        } else {
            // create new user
            final UserLdap newUser = userMapper.userToUserLdap(user);
            return processUserCreation(newUser, request.getDeviceInfo());
        }
    }

    /**
     * Handles user registration in system with specified social provider
     *
     * @param request - registration request contains social user information and device data for set email language
     * @return - HTTP response with registration error or success details
     */
    @ApiOperation(value = "${RegistrationController.socialRegistration.value}",
        response = ResponseEntity.class,
        notes = "${RegistrationController.socialRegistration.notes}")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Success|OK"),
        @ApiResponse(code = 500, message = "Account with this social id already exists")
    })
    @ApiImplicitParams(
        @ApiImplicitParam(name = "request",
            value = "${RegistrationController.socialRegistration.socialRegisterRequest}",
            required = true, dataType = "SocialRegisterRequest")
    )
    @RequestMapping(ApiEndpoints.socialLogin)
    public @NotNull ResponseEntity<?> socialRegistration(@RequestBody @NotNull SocialRegisterRequest request) {

        final User user = request.getUser();
        final String email = user.getEmail();
        final String id = request.getId();
        final String provider = request.getSocialProvider();

        //verify if user exists in db and throw error, else create
        final UserLdap userFound = userService.getUserBySocialId(id, provider);
        if (userFound != null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Account with this social id already exists");
        } else {
            if (email != null) {
                final UserLdap existedUser = userService.getUserByEmail(email);
                // same emails not allowed
                if (existedUser != null) {
                    // case when confirmation expired and confirm impossible anymore
                    if (existedUser.accountConfirmationIsExpired()) {
                        final UserLdap newUser = userMapper.userToUserLdap(user);
                        newUser.prepareSocialUser(id, provider);
                        newUser.setUserPassword(generateCommonLangPassword());

                        userService.deleteUser(newUser);
                        return processUserCreation(newUser, request.getDeviceInfo());
                    }
                    if (existedUser.accountConfirmationValid()) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("Social account with this email already exists and waiting for confirmation");
                    }

                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Social account with this email already exists");
                }
            }
            // else create new user
            final UserLdap newUser = userMapper.userToUserLdap(user);
            newUser.prepareSocialUser(id, provider);

            return processUserCreation(newUser, request.getDeviceInfo());
        }
    }

    /**
     * Method creates new user in database and forms a message for account confirmation
     * (additional common registration part)
     *
     * @param newUser    - user to be saved in database
     * @param deviceInfo - user device information for region targeting
     * @return HTTP response with registration error or success details
     */
    private @NotNull ResponseEntity<?> processUserCreation(@NotNull UserLdap newUser, DeviceInfo deviceInfo) {
        userService.createUser(newUser);

        final UserLdap createdUser = userService.findById(newUser.getUid());
        if (createdUser == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("User was not created");
        } else {
            if (createdUser.getEmail() != null) {
                createConfirmation(createdUser, deviceInfo);
            }
            return ResponseEntity.ok(
                new RegistrationSuccessResponse(true, createdUser.getEmail() != null));
        }
    }

    /**
     * Method creates user registration confirmation
     *
     * @param createdUser - target user
     * @param deviceInfo  - user device information for region targeting
     */
    private void createConfirmation(@NotNull UserLdap createdUser, DeviceInfo deviceInfo) {
        // Create token
        @NotNull ConfirmationRecord confirmationRecord = new ConfirmationRecord(createdUser.getUid(),
            AccountOperation.ACCOUNT_CONFIRMATION);
        // Save it
        confirmationTokenService.save(confirmationRecord);
        // Send email
        try {
            emailSenderService.sendRegistrationConfirmMail(createdUser, confirmationRecord, deviceInfo);
        } catch (@NotNull IOException | MessagingException e) {
            e.printStackTrace();
        }
    }
}


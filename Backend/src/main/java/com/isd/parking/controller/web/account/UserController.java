package com.isd.parking.controller.web.account;

import com.isd.parking.models.enums.AccountState;
import com.isd.parking.models.users.User;
import com.isd.parking.models.users.UserLdap;
import com.isd.parking.models.users.UserMapper;
import com.isd.parking.security.AccountConfirmationPeriods;
import com.isd.parking.security.PasswordEncoding.CustomBcryptPasswordEncoder;
import com.isd.parking.security.model.AccountOperation;
import com.isd.parking.security.model.ConfirmationRecord;
import com.isd.parking.security.model.payload.*;
import com.isd.parking.service.RestService;
import com.isd.parking.service.implementations.ConfirmationServiceImpl;
import com.isd.parking.service.implementations.EmailSenderServiceImpl;
import com.isd.parking.storage.ldap.UserServiceImpl;
import com.isd.parking.utils.AppDateUtils;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

import static com.isd.parking.controller.ApiEndpoints.*;
import static com.isd.parking.models.users.UserLdap.getUserLdapProperty;
import static com.isd.parking.models.users.UserLdap.userLdapClassFieldsList;
import static com.isd.parking.storage.ldap.LdapConstants.USER_UID_ATTRIBUTE;
import static com.isd.parking.utils.AppDateUtils.isDateBeforeNow;
import static org.springframework.web.bind.annotation.RequestMethod.POST;


/**
 * Provides methods for
 * - Google captcha processing,
 * - user account confirmation
 * - forgot and reset password handle
 * - parking lot reservation and parking lot cancel reservation by administrator
 */
@RestController
@CrossOrigin(origins = "*")
@Slf4j
public class UserController {

    private final UserServiceImpl userService;

    private final RestService restService;

    private final ConfirmationServiceImpl confirmationTokenService;

    private final EmailSenderServiceImpl emailSenderService;

    private final UserMapper userMapper;

    @Value("${spring.ldap.base}")
    private String ldapSearchBase;

    @Value("${spring.mail.from.email}")
    private String from;

    @Autowired
    public UserController(UserServiceImpl userService,
                          RestService restService,
                          ConfirmationServiceImpl confirmationTokenService,
                          EmailSenderServiceImpl emailSenderService,
                          UserMapper userMapper) {
        this.userService = userService;
        this.restService = restService;
        this.confirmationTokenService = confirmationTokenService;
        this.emailSenderService = emailSenderService;
        this.userMapper = userMapper;
    }

    /**
     * Handles Google re-captcha flow
     *
     * @param grecaptcha - captcha data from client web application
     * @return HTTP response with user validation error or success details
     * sample message
     * {
     * "success": true,
     * "challenge_ts": "2020-03-07T13:40:25Z",
     * "hostname": "localhost"
     * }
     */
    @ResponseBody
    @RequestMapping(validateCaptcha)
    public ResponseEntity<?> grecaptcha(@RequestBody String grecaptcha) {
        final String grecaptchaToken = new JSONObject(grecaptcha).getJSONObject("grecaptcha").getString("token");

        if (grecaptchaToken == null || grecaptchaToken.equals("")) {
            return ResponseEntity.ok(RecaptchaResponse.builder()
                .success(false)
                .challenge_ts(new Date(System.currentTimeMillis()))
                .hostname("localhost")
                .message("Token is empty or invalid").build());
        } else {
            String secretKey = RecaptchaResponse.GoogleRecaptchaConstants.SECRET_KEY;
            String url = String.format(RecaptchaResponse.GoogleRecaptchaConstants.GRECAPTCHA_API_URL, secretKey, grecaptchaToken);
            // send captcha data to Google service for validation
            String response = restService.getPlainJSON(url);

            return ResponseEntity.ok(response);
        }
    }


    /**
     * User account confirmation handler
     *
     * @param confirmRequest - user account confirmation token
     * @return HTTP response with user validation error or success details
     */
    @RequestMapping(value = confirmAction, method = POST)
    public ResponseEntity<?> confirmUserAccount(@RequestBody String confirmRequest) {

        final String confirmationToken = new JSONObject(confirmRequest).getString("confirmationToken");
        Optional<ConfirmationRecord> optionalConfirmationToken = confirmationTokenService.findByConfirmationToken(confirmationToken);

        // if this confirmation token exists in database
        if (optionalConfirmationToken.isPresent()) {
            ConfirmationRecord token = optionalConfirmationToken.get();

            // if token was not expired
            if (confirmationTokenService.assertNotExpired(token)) {
                // if token was not used
                if (!token.isClaimed()) {
                    UserLdap user = userService.findById(optionalConfirmationToken.get().getUid());
                    if (token.getOperationType() == AccountOperation.ACCOUNT_CONFIRMATION) {
                        userService.updateUser(user.getUid(), "accountState", String.valueOf(AccountState.ENABLED));
                    }
                    token.setClaimed(true);
                    confirmationTokenService.save(token);

                    return ResponseEntity.ok(new ActionSuccessResponse(true));
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Confirmation token is already used. Login or register again");
                }
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Confirmation token is expired. Register again");
            }
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Confirmation token not found. Register again");
        }
    }

    /**
     * User forgot password handler receives the user address and send an reset password email
     *
     * @param request - forgot password initial request contains user email and device data for set email language
     * @return HTTP response with user forgot password initial processing error or success email send details
     * @throws IOException
     * @throws MessagingException
     */
    @RequestMapping(value = forgotPassword, method = POST)
    public ResponseEntity<?> forgotUserPassword(@RequestBody ForgotPassRequest request)
        throws IOException, MessagingException {

        final String userEmail = request.getEmail();
        UserLdap existingUser = userService.getUserByEmail(userEmail);

        if (existingUser != null) {
            Optional<ConfirmationRecord> lastConfirmationToken = confirmationTokenService.findLastByUsername(existingUser.getUid());

            if (lastConfirmationToken.isPresent()) {
                ConfirmationRecord token = lastConfirmationToken.get();

                // check if user password reset is allowed
                if (!confirmationTokenService.assertValidForRepeat(token)) {
                    String periodEnding = AppDateUtils.getPeriodEnding(AccountConfirmationPeriods.REQUEST_CONFIRMATION_TOKEN_PERIOD_IN_DAYS);
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Password reset request is allowed only " + periodEnding);
                }
            }
            // Create token
            ConfirmationRecord confirmationRecord = new ConfirmationRecord(existingUser.getUid(), AccountOperation.PASSWORD_RESET);
            // Save it
            confirmationTokenService.save(confirmationRecord);
            // Send email
            emailSenderService.sendPassResetMail(existingUser, confirmationRecord, request.getDeviceInfo());

            return ResponseEntity.ok(new ActionSuccessResponse(true));

        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("User with this email doesn't exists on the server");
        }
    }


    /**
     * Endpoint to update a user's password
     *
     * @param resetPasswordRequest - request contains all necessary user data - confirmation token
     *                             for retrieve user uid and check validation period,
     *                             new password and device information
     * @return HTTP response with user password reset processing error or success details
     */
    @RequestMapping(value = resetPassword, method = POST)
    public ResponseEntity<?> resetUserPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {

        final String confirmationToken = resetPasswordRequest.getResetDetails().getConfirmationToken();
        final String password = resetPasswordRequest.getResetDetails().getPassword();

        Optional<ConfirmationRecord> optionalConfirmationToken = confirmationTokenService.findByConfirmationToken(confirmationToken);

        if (optionalConfirmationToken.isPresent()) {
            ConfirmationRecord token = optionalConfirmationToken.get();

            if (confirmationTokenService.assertNotExpired(token)) {
                final String username = token.getUid();
                if (username != null) {
                    // Use username to find user
                    UserLdap user = userService.findById(username);
                    if (user != null) {
                        LocalDateTime passwordUpdatedAt = userService.getPasswordUpdateAt(user.getUid());
                        // check if password change is allowed
                        if (assertPassValidForChange(passwordUpdatedAt)) {
                            userService.updateUserPassword(user, new CustomBcryptPasswordEncoder().encode(password));

                            return ResponseEntity.ok(new ActionSuccessResponse(true));
                        } else {
                            String periodEnding = AppDateUtils.getPeriodEnding(AccountConfirmationPeriods.RESET_PASSWORD_PERIOD_IN_DAYS);
                            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Password change is allowed only " + periodEnding);
                        }
                    } else {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("User with this username doesn't exists on the server");
                    }
                } else {
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

    /**
     * Checks if password is allowed for changing conform password reset period in days
     *
     * @param passwordUpdatedAt - password lats updated date
     * @return operation result
     */
    private boolean assertPassValidForChange(LocalDateTime passwordUpdatedAt) {
        return isDateBeforeNow(passwordUpdatedAt.plusDays(AccountConfirmationPeriods.RESET_PASSWORD_PERIOD_IN_DAYS),
            AccountConfirmationPeriods.MAX_CLOCK_SKEW_MINUTES);
    }

    /**
     * Endpoint to retrieve user profile from LDAP database
     *
     * @param username - target username
     * @return target user
     */
    @ResponseBody
    @PostMapping("/" + profile)
    public User getUserByUsername(@RequestBody String username) {
        UserLdap userFound = userService.findById(username);

        return userMapper.userLdapToUser(userFound);
    }

    /**
     * Endpoint to modifiyng user profile in LDAP database
     *
     * @param updateUserRequest - request with user information for modifying
     * @return HTTP response with user password reset processing error or success details
     */
    @ResponseBody
    @PostMapping("/" + profileUpdate)
    public ResponseEntity<?> updateUser(@RequestBody UpdateUserRequest updateUserRequest) {

        final User user = updateUserRequest.getUser();
        String username = updateUserRequest.getUsername();
        UserLdap userFound = userService.findById(updateUserRequest.getUsername());

        if (userFound != null) {
            // map API input user to LDAP user
            UserLdap userForUpdate = userMapper.userToUserLdap(user);

            // update user attribute based on its availability in the user modifying request
            for (String field : userLdapClassFieldsList) {
                Object propertyValue = getUserLdapProperty(userForUpdate, field);
                // if this field is present user modifying request update it
                if (propertyValue != null) {
                    if (field.equals(USER_UID_ATTRIBUTE)) {
                        userService.updateUsername(username, String.valueOf(propertyValue));
                        username = String.valueOf(propertyValue);
                    } else {
                        userService.updateUser(username, field, String.valueOf(propertyValue));
                    }
                }
            }
            return ResponseEntity.ok(new ActionSuccessResponse(true));

        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("User doesn't exists on the server");
        }
    }

    /**
     * Endpoint to delete user profile from LDAP database
     *
     * @param uid - target uid
     * @return HTTP response with user deleting error or success details
     */
    @ResponseBody
    @PostMapping("/" + profileDelete)
    public ResponseEntity<?> deleteUser(@RequestBody String uid) {

        UserLdap userFound = userService.findById(uid);
        if (userFound != null) {
            userService.deleteUserById(uid);
            return ResponseEntity.ok(new ActionSuccessResponse(true));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("User doesn't exists on the server");
        }
    }

    /**
     * Endpoint to retrieve all users from LDAP database
     *
     * @return users list
     */
    @ResponseBody
    @GetMapping("/" + users)
    public Iterable<UserLdap> getAllUsers() {
        return userService.findAll();
    }
}

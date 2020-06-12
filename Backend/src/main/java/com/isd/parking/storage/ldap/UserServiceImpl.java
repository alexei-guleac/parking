package com.isd.parking.storage.ldap;

import com.isd.parking.config.locale.SmartLocaleResolver;
import com.isd.parking.models.users.SocialUser;
import com.isd.parking.models.users.User;
import com.isd.parking.models.users.UserLdap;
import com.isd.parking.models.users.UserMapper;
import com.isd.parking.services.UserService;
import com.isd.parking.web.rest.payload.ActionSuccessResponse;
import com.isd.parking.web.rest.payload.ResponseEntityFactory;
import com.isd.parking.web.rest.payload.account.UpdateUserRequest;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import javax.naming.directory.DirContext;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.isd.parking.models.users.UserLdap.getUserLdapProperty;
import static com.isd.parking.models.users.UserLdap.userLdapClassFieldsList;
import static com.isd.parking.storage.ldap.LdapConstants.USER_SOCIALS_ATTRIBUTE;
import static com.isd.parking.storage.ldap.LdapConstants.USER_UID_ATTRIBUTE;


/**
 * Provides methods for user account manipulating in .ldif file and in-memory LDAP server
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    private final SmartLocaleResolver localeResolver;

    private final ResponseEntityFactory responseEntityFactory;

    private final UserLdapClient userLdapClient;

    private final UserLdapFileService userLdapFileService;

    @Autowired
    public UserServiceImpl(UserMapper userMapper,
                           SmartLocaleResolver localeResolver,
                           ResponseEntityFactory responseEntityFactory,
                           UserLdapClient userLdapClient,
                           UserLdapFileService userLdapFileService) {
        this.userMapper = userMapper;
        this.localeResolver = localeResolver;
        this.responseEntityFactory = responseEntityFactory;
        this.userLdapClient = userLdapClient;
        this.userLdapFileService = userLdapFileService;
    }

    /**
     * Retrieve user profile from database
     *
     * @param username - target username
     * @return target user
     */
    public SocialUser getUserByUsername(String username) {
        UserLdap userFound = this.findById(username);
        if (userFound == null) {
            return null;
        } else {
            return userMapper.userLdapToSocialUser(userFound);
        }
    }

    /**
     * Create new user in LDAP server
     *
     * @param user - target user for creating
     * @return operation result
     */
    public boolean createUser(@NotNull UserLdap user) {
        user.setUserRegistered();

        // save in-memory server
        userLdapClient.createUser(user);
        // save to .ldif file
        userLdapFileService.save(user);

        return true;
    }

    /**
     * Modifying user profile in database
     *
     * @param updateUserRequest - request with user information for modifying
     * @return HTTP response with user password reset processing error or success details
     */
    public @NotNull ResponseEntity<?> updateUser(@NotNull UpdateUserRequest updateUserRequest,
                                                 Map<String, String> headers) {

        final User user = updateUserRequest.getUser();
        String username = updateUserRequest.getUsername();
        final UserLdap userFound = this.findById(updateUserRequest.getUsername());
        final Locale locale = localeResolver.resolveLocale(headers);

        if (userFound != null) {
            // map input user to LDAP user
            final UserLdap userForUpdate = userMapper.userToUserLdap(user);

            // update user attribute based on its availability in the user modifying request
            for (@NotNull String field : userLdapClassFieldsList) {
                Object propertyValue = getUserLdapProperty(userForUpdate, field);
                // if this field is present user modifying request update it
                if (propertyValue != null) {
                    if (field.equals(USER_UID_ATTRIBUTE)) {
                        this.updateUsername(username, String.valueOf(propertyValue));
                        username = String.valueOf(propertyValue);
                    } else if (field.equals(USER_SOCIALS_ATTRIBUTE)) {
                        continue;
                    } else {
                        log.info(field + " " + propertyValue);
                        this.updateUser(username, field, String.valueOf(propertyValue));
                    }
                }
            }
            return ResponseEntity.ok(new ActionSuccessResponse(true));

        } else {
            return responseEntityFactory.userNotExists(locale);
        }
    }

    /**
     * Update user in LDAP server
     *
     * @param user - target user for updating
     * @return operation result
     */
    public boolean updateUser(@NotNull UserLdap user) {
        // upd in in-memory server
        userLdapClient.updateUser(user);
        // update in .ldif file
        userLdapFileService.update(user);

        return true;
    }

    /**
     * Modify LDAP user attribute value
     *
     * @param username       - target user id
     * @param attributeName  - specified attribute name
     * @param attributeValue - specified attribute value
     * @return operation result
     */
    public boolean updateUser(@NotNull String username,
                              @Nullable String attributeName,
                              @Nullable String attributeValue) {
        // upd in in-memory server
        userLdapClient.updateUser(username, attributeName, attributeValue, DirContext.REPLACE_ATTRIBUTE);
        // update in .ldif file
        userLdapFileService.update(username, attributeName, attributeValue);

        return true;
    }

    /**
     * Modify LDAP user name (in this case uid)
     *
     * @param username    - target user unique username
     * @param newUsername - user new uid
     * @return operation result
     */
    public boolean updateUsername(@NotNull String username, @NotNull String newUsername) {
        // upd in in-memory server
        userLdapClient.updateUsername(username, newUsername);
        // update in .ldif file
        userLdapFileService.update(username, USER_UID_ATTRIBUTE, newUsername);

        return true;
    }

    /**
     * Modify LDAP user password
     *
     * @param user     - target LDAP user
     * @param password - target user password
     * @return operation result
     */
    public boolean updateUserPassword(@NotNull UserLdap user, String password) {
        // upd in in-memory server
        userLdapClient.updateUserPassword(user, password);
        // update in .ldif file
        userLdapFileService.updateUserPassword(user.getUid(), password);

        return true;
    }

    /**
     * Delete user profile from database
     *
     * @param username - target username
     * @return HTTP response with user deleting error or success details
     */
    public @NotNull ResponseEntity<?> deleteUser(String username,
                                                 Map<String, String> headers) {

        final Locale locale = localeResolver.resolveLocale(headers);
        UserLdap userFound = this.findById(username);

        if (userFound != null) {
            this.deleteUserById(username);
            return ResponseEntity.ok(new ActionSuccessResponse(true));
        } else {
            return responseEntityFactory.userNotExists(locale);
        }
    }

    /**
     * Delete user from LDAP server
     *
     * @param user - target user
     * @return operation result
     */
    public boolean deleteUser(@NotNull UserLdap user) {
        // del from in-memory server
        userLdapClient.deleteUser(user);
        // delete from .ldif file
        userLdapFileService.deleteUser(user);

        return true;
    }

    /**
     * Delete user by id from LDAP server
     *
     * @param username - target user username
     * @return operation result
     */
    public boolean deleteUserById(@NotNull String username) {
        // del from in-memory server
        userLdapClient.deleteUserById(username);
        // delete from .ldif file
        userLdapFileService.deleteUserById(username);

        return true;
    }

    /**
     * Retrieve user membership authorities by user id
     * Used for setting JWT
     *
     * @param username - target user username
     * @return string representation of user membership authorities
     */
    public @org.jetbrains.annotations.Nullable String getAuthoritiesMembershipById(final String username) {
        return userLdapClient.getAuthoritiesMembershipById(username);
    }

    /**
     * Search user by id (make sure it is available on server)
     *
     * @param username - target user username
     * @return operation result
     */
    public boolean searchUser(final String username) {
        return userLdapClient.searchUser(username);
    }

    /**
     * Get user by id
     *
     * @param username - target user username
     * @return user found
     */
    public @org.jetbrains.annotations.Nullable UserLdap findById(@NotNull String username) {
        return userLdapClient.findById(username);
    }

    /**
     * Get all users request method
     *
     * @return - list of all users
     */
    public List<UserLdap> findAll() {
        return userLdapClient.findAll();
    }

    /**
     * Search user by email (make sure it is available on server)
     *
     * @param email - target user's email
     * @return operation result
     */
    public boolean searchUsersByEmail(final String email) {
        return userLdapClient.searchUsersByEmail(email);
    }

    /**
     * Get user by given social provider id
     *
     * @param id     - target user social id
     * @param social - social service provider
     * @return user found
     */
    public @org.jetbrains.annotations.Nullable UserLdap getUserBySocialId(String id, String social) {
        return userLdapClient.getUserBySocialId(id, social);
    }

    /**
     * Get user with specified email
     *
     * @param email - target user email
     * @return user found
     */
    public @org.jetbrains.annotations.Nullable UserLdap getUserByEmail(String email) {
        return userLdapClient.getUserByEmail(email);
    }

    /**
     * Get passwordUpdatedAt user LDAP entry attribute value
     *
     * @param username - target user username
     * @return password updated at date
     */
    public @org.jetbrains.annotations.Nullable LocalDateTime getPasswordUpdateAt(String username) {
        return userLdapClient.getPasswordUpdateAt(username);
    }

    /**
     * Connect given social provider id to user account
     *
     * @param id       - target user social id
     * @param provider - social service provider
     * @return operation result
     */
    public boolean connectSocialProvider(String username, String provider, String id) {
        String socialProvider = provider + "id";
        // connect in in-memory server
        userLdapClient.connectSocialProvider(username, socialProvider, id);
        // connect in .ldif file
        userLdapFileService.connectSocialProvider(username, socialProvider, id);

        return true;
    }

    /**
     * Disconnect given social provider from user account
     *
     * @param username - target user username
     * @param provider - social service provider
     * @return operation result
     */
    public boolean disconnectSocialProvider(String username, String provider) {
        String socialProvider = provider + "id";
        // disconnect in in-memory server
        userLdapClient.disconnectSocialProvider(username, socialProvider);
        // disconnect in .ldif file
        userLdapFileService.dicconnectSocialProvider(username, socialProvider);

        return true;
    }
}

package com.isd.parking.storage.ldap;

import com.isd.parking.models.users.UserLdap;
import com.isd.parking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.time.LocalDateTime;
import java.util.List;

import static com.isd.parking.storage.ldap.LdapConstants.USER_UID_ATTRIBUTE;


/**
 * Provides methods for user account manipulating in .ldif file and in-memory LDAP server
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserLdapClient userLdapClient;

    private final UserLdapFileService userLdapFileService;

    @Autowired
    public UserServiceImpl(UserLdapClient userLdapClient, UserLdapFileService userLdapFileService) {
        this.userLdapClient = userLdapClient;
        this.userLdapFileService = userLdapFileService;
    }

    /**
     * Create new user in LDAP server
     *
     * @param user - target user for creating
     * @return operation result
     */
    public boolean createUser(UserLdap user) {
        user.setUserRegistered();

        // save in-memory server
        userLdapClient.createUser(user);
        // save to .ldif file
        userLdapFileService.save(user);

        return true;
    }

    /**
     * Update user in LDAP server
     *
     * @param user - target user for updating
     * @return operation result
     */
    public boolean updateUser(UserLdap user) {
        // upd in in-memory server
        userLdapClient.updateUser(user);
        // update in .ldif file
        userLdapFileService.update(user);

        return true;
    }

    /**
     * Modify LDAP user attribute value
     *
     * @param uid            - target user id
     * @param attributeName  - specified attribute name
     * @param attributeValue - specified attribute value
     * @return operation result
     */
    public boolean updateUser(String uid,
                              @Nullable String attributeName,
                              @Nullable String attributeValue) {
        // upd in in-memory server
        userLdapClient.updateUser(uid, attributeName, attributeValue);
        // update in .ldif file
        userLdapFileService.update(uid, attributeName, attributeValue);

        return true;
    }

    /**
     * Modify LDAP user name (in this case uid)
     *
     * @param username    - target user unique username
     * @param newUsername - user new uid
     * @return operation result
     */
    public boolean updateUsername(String username, String newUsername) {
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
    public boolean updateUserPassword(UserLdap user, String password) {
        // upd in in-memory server
        userLdapClient.updateUserPassword(user, password);
        // update in .ldif file
        userLdapFileService.updateUserPassword(user.getUid(), password);

        return true;
    }

    /**
     * Delete user from LDAP server
     *
     * @param user - target user
     * @return operation result
     */
    public boolean deleteUser(UserLdap user) {
        // del from in-memory server
        userLdapClient.deleteUser(user);
        // delete from .ldif file
        userLdapFileService.deleteUser(user);

        return true;
    }

    /**
     * Delete user by id from LDAP server
     *
     * @param uid - target user id
     * @return operation result
     */
    public boolean deleteUserById(String uid) {
        // del from in-memory server
        userLdapClient.deleteUserById(uid);
        // delete from .ldif file
        userLdapFileService.deleteUserById(uid);

        return true;
    }

    /**
     * Retrieve user membership authorities by user id
     * Used for setting JWT
     *
     * @param uid - target user id
     * @return string representation of user membership authorities
     */
    public String getAuthoritiesMembershipById(final String uid) {
        return userLdapClient.getAuthoritiesMembershipById(uid);
    }

    /**
     * Search user by id (make sure it is available on server)
     *
     * @param uid - target user id
     * @return operation result
     */
    public boolean searchUser(final String uid) {
        return userLdapClient.searchUser(uid);
    }

    /**
     * Get user by id
     *
     * @param uid - target user id
     * @return user found
     */
    public UserLdap findById(String uid) {
        return userLdapClient.findById(uid);
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
    public UserLdap getUserBySocialId(String id, String social) {
        return userLdapClient.getUserBySocialId(id, social);
    }

    /**
     * Get user with specified email
     *
     * @param email - target user email
     * @return user found
     */
    public UserLdap getUserByEmail(String email) {
        return userLdapClient.getUserByEmail(email);
    }

    /**
     * Get passwordUpdatedAt user LDAP entry attribute value
     *
     * @param uid - target user id
     * @return password updated at date
     */
    public LocalDateTime getPasswordUpdateAt(String uid) {
        return userLdapClient.getPasswordUpdateAt(uid);
    }
}

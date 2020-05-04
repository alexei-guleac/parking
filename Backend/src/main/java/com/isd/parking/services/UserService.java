package com.isd.parking.services;

import com.isd.parking.models.users.UserLdap;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.util.List;


@Service
public interface UserService {

    /**
     * Create new user
     *
     * @param user - target user for creating
     * @return operation result
     */
    boolean createUser(UserLdap user);

    /**
     * Update user
     *
     * @param user - target user for updating
     * @return operation result
     */
    boolean updateUser(UserLdap user);

    /**
     * Modify user attribute value
     *
     * @param uid            - target user id
     * @param attributeName  - specified attribute name
     * @param attributeValue - specified attribute value
     * @return operation result
     */
    boolean updateUser(String uid,
                       @Nullable String attributeName,
                       @Nullable String attributeValue);

    /**
     * Modify user name
     *
     * @param username    - target user unique username
     * @param newUsername - user new username
     * @return operation result
     */
    boolean updateUsername(String username, String newUsername);

    /**
     * Modify user password
     *
     * @param user     - target LDAP user
     * @param password - target user password
     * @return operation result
     */
    boolean updateUserPassword(UserLdap user, String password);

    /**
     * Delete user from server
     *
     * @param user - target user
     * @return operation result
     */
    boolean deleteUser(UserLdap user);

    /**
     * Delete user from server by id
     *
     * @param uid - target user id
     * @return operation result
     */
    boolean deleteUserById(String uid);

    /**
     * Search user by id (make sure it is available on server)
     *
     * @param uid - target user id
     * @return operation result
     */
    boolean searchUser(final String uid);

    /**
     * Get user by id
     *
     * @param uid - target user id
     * @return user found
     */
    @org.jetbrains.annotations.Nullable UserLdap findById(String uid);

    /**
     * Get all users
     *
     * @return - list of all users
     */
    List<UserLdap> findAll();

    /**
     * Search user by email (make sure it is available on server)
     *
     * @param email - target user's email
     * @return operation result
     */
    boolean searchUsersByEmail(final String email);

    /**
     * Get user by given social provider id
     *
     * @param id     - target user social id
     * @param social - social service provider
     * @return user found
     */
    @org.jetbrains.annotations.Nullable UserLdap getUserBySocialId(String id, String social);

    /**
     * Get user with specified email
     *
     * @param email - target user email
     * @return user found
     */
    @org.jetbrains.annotations.Nullable UserLdap getUserByEmail(String email);
}

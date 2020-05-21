package com.isd.parking.web.rest.controllers.account;

import com.isd.parking.config.SwaggerConfig;
import com.isd.parking.models.users.SocialUser;
import com.isd.parking.models.users.User;
import com.isd.parking.models.users.UserLdap;
import com.isd.parking.models.users.UserMapper;
import com.isd.parking.storage.ldap.UserServiceImpl;
import com.isd.parking.web.rest.ApiEndpoints;
import com.isd.parking.web.rest.payload.ActionSuccessResponse;
import com.isd.parking.web.rest.payload.account.UpdateUserRequest;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.isd.parking.models.users.UserLdap.getUserLdapProperty;
import static com.isd.parking.models.users.UserLdap.userLdapClassFieldsList;
import static com.isd.parking.storage.ldap.LdapConstants.USER_SOCIALS_ATTRIBUTE;
import static com.isd.parking.storage.ldap.LdapConstants.USER_UID_ATTRIBUTE;


/**
 * Provides methods for users
 * CRUD operations in database as well as get all users information from database
 */
@Api(value = "User Controller",
    description = "Operations pertaining to User entity in database")
@RestController
@CrossOrigin(origins = "*")
@Slf4j
public class UserController {

    private final UserServiceImpl userService;

    private final UserMapper userMapper;

    @Autowired
    public UserController(UserServiceImpl userService,
                          UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    /**
     * Endpoint to retrieve user profile from database
     *
     * @param username - target username
     * @return target user
     */
    @ApiOperation(value = "${UserController.getUserByUsername.value}",
        response = SocialUser.class,
        notes = "${UserController.getUserByUsername.notes}",
        authorizations = {@Authorization(value = SwaggerConfig.TOKEN_TYPE)})
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Success|OK"),
        @ApiResponse(code = 500, message = "Server error")
    })
    @ApiImplicitParams(
        @ApiImplicitParam(name = "username",
            value = "${UserController.getUserByUsername.username}",
            required = true, dataType = "String")
    )
    @ResponseBody
    @PostMapping("/" + ApiEndpoints.profile)
    public SocialUser getUserByUsername(@RequestBody String username) {
        UserLdap userFound = userService.findById(username);
        if (userFound == null) {
            return null;
        } else {
            return userMapper.userLdapToSocialUser(userFound);
        }
    }

    /**
     * Endpoint to modifying user profile in database
     *
     * @param updateUserRequest - request with user information for modifying
     * @return HTTP response with user password reset processing error or success details
     */
    @ApiOperation(value = "${UserController.updateUser.value}",
        response = ResponseEntity.class,
        notes = "${UserController.updateUser.notes}",
        authorizations = {@Authorization(value = SwaggerConfig.TOKEN_TYPE)})
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Success|OK"),
        @ApiResponse(code = 500, message = "User doesn't exists on the server")
    })
    @ApiImplicitParams(
        @ApiImplicitParam(name = "updateUserRequest",
            value = "${UserController.updateUser.updateUserRequest}",
            required = true, dataType = "UpdateUserRequest")
    )
    @ResponseBody
    @PostMapping("/" + ApiEndpoints.profileUpdate)
    public @NotNull ResponseEntity<?> updateUser(@RequestBody @NotNull UpdateUserRequest updateUserRequest) {

        final User user = updateUserRequest.getUser();
        String username = updateUserRequest.getUsername();
        final UserLdap userFound = userService.findById(updateUserRequest.getUsername());

        if (userFound != null) {
            // map input user to LDAP user
            final UserLdap userForUpdate = userMapper.userToUserLdap(user);

            // update user attribute based on its availability in the user modifying request
            for (@NotNull String field : userLdapClassFieldsList) {
                Object propertyValue = getUserLdapProperty(userForUpdate, field);
                // if this field is present user modifying request update it
                if (propertyValue != null) {
                    if (field.equals(USER_UID_ATTRIBUTE)) {
                        userService.updateUsername(username, String.valueOf(propertyValue));
                        username = String.valueOf(propertyValue);
                    } else if (field.equals(USER_SOCIALS_ATTRIBUTE)) {
                        continue;
                    } else {
                        log.info(field + " " + propertyValue);
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
     * Endpoint to delete user profile from database
     *
     * @param username - target username
     * @return HTTP response with user deleting error or success details
     */
    @ApiOperation(value = "${UserController.deleteUser.value}",
        response = ResponseEntity.class,
        notes = "${UserController.deleteUser.notes}",
        authorizations = {@Authorization(value = SwaggerConfig.TOKEN_TYPE)})
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Success|OK"),
        @ApiResponse(code = 500, message = "User doesn't exists on the server")
    })
    @ApiImplicitParams(
        @ApiImplicitParam(name = "username",
            value = "${UserController.deleteUser.username}", required = true, dataType = "String")
    )
    @ResponseBody
    @PostMapping("/" + ApiEndpoints.profileDelete)
    public @NotNull ResponseEntity<?> deleteUser(@RequestBody String username) {

        UserLdap userFound = userService.findById(username);
        if (userFound != null) {
            userService.deleteUserById(username);
            return ResponseEntity.ok(new ActionSuccessResponse(true));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("User doesn't exists on the server");
        }
    }

    /**
     * Endpoint to retrieve all users from database
     *
     * @return users list
     */
    @ApiOperation(value = "${UserController.getAllUsers.value}",
        response = UserLdap.class,
        responseContainer = "Iterable",
        notes = "${UserController.getAllUsers.notes}",
        authorizations = {@Authorization(value = SwaggerConfig.TOKEN_TYPE)})
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Success|OK")
    })
    @ResponseBody
    @GetMapping("/" + ApiEndpoints.users)
    public Iterable<UserLdap> getAllUsers() {
        return userService.findAll();
    }
}

package com.isd.parking.web.rest.controllers.account;

import com.isd.parking.config.SwaggerConfig;
import com.isd.parking.models.users.SocialUser;
import com.isd.parking.models.users.UserLdap;
import com.isd.parking.storage.ldap.UserServiceImpl;
import com.isd.parking.web.rest.ApiEndpoints;
import com.isd.parking.web.rest.payload.account.UpdateUserRequest;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


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

    @Autowired
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
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
    @PostMapping(ApiEndpoints.profile)
    public SocialUser getUserByUsername(@RequestBody String username) {
        return userService.getUserByUsername(username);
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
    @PostMapping(ApiEndpoints.profileUpdate)
    public @NotNull ResponseEntity<?> updateUser(@RequestBody @NotNull UpdateUserRequest updateUserRequest,
                                                 @RequestHeader Map<String, String> headers) {
        return userService.updateUser(updateUserRequest, headers);
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
    @PostMapping(ApiEndpoints.profileDelete)
    public @NotNull ResponseEntity<?> deleteUser(@RequestBody String username,
                                                 @RequestHeader Map<String, String> headers) {
        return userService.deleteUser(username, headers);
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
    @GetMapping(ApiEndpoints.users)
    public Iterable<UserLdap> getAllUsers() {
        return userService.findAll();
    }
}

package com.isd.parking.web.rest.controllers.account;

import com.isd.parking.services.RegistrationService;
import com.isd.parking.web.rest.ApiEndpoints;
import com.isd.parking.web.rest.payload.account.register.RegistrationRequest;
import com.isd.parking.web.rest.payload.account.register.SocialRegisterRequest;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

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

    private final RegistrationService registrationService;

    @Autowired
    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
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
    public @NotNull ResponseEntity<?> registration(@RequestBody @NotNull RegistrationRequest request,
                                                   @RequestHeader Map<String, String> headers) {
        return registrationService.registration(request, headers);
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
    public @NotNull ResponseEntity<?> socialRegistration(@RequestBody @NotNull SocialRegisterRequest request,
                                                         @RequestHeader Map<String, String> headers) {
        return registrationService.socialRegistration(request, headers);
    }
}


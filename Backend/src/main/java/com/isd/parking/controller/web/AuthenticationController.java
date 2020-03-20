package com.isd.parking.controller.web;

import com.isd.parking.models.UserLdap;
import com.isd.parking.models.enums.AccountState;
import com.isd.parking.security.model.payload.AuthenticationRequest;
import com.isd.parking.security.model.payload.AuthenticationResponse;
import com.isd.parking.security.model.payload.SocialAuthRequest;
import com.isd.parking.security.model.payload.SocialAuthResponse;
import com.isd.parking.service.ConfirmationTokenService;
import com.isd.parking.service.EmailSenderService;
import com.isd.parking.service.ldap.UserLdapClient;
import com.isd.parking.utils.AppFileUtils;
import com.isd.parking.utils.ColorConsoleOutput;
import com.nimbusds.jose.JOSEException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import static com.isd.parking.security.JwtUtils.generateHMACToken;
import static com.isd.parking.utils.ColorConsoleOutput.blTxt;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("auth")
@Slf4j
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;

    private final UserLdapClient userLdapClient;

    private final ConfirmationTokenService confirmationTokenService;

    private final EmailSenderService emailSenderService;

    private final ColorConsoleOutput console;

    private final String secretKeyFile = "secret.key";

    private final String secret = new AppFileUtils().getResourceAsString(secretKeyFile);

    @Value("${spring.ldap.base}")
    private String ldapSearchBase;

    // private final int expirationInMinutes = 24 * 60;
    private final int expirationInMinutes = 10;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager,
                                    UserLdapClient userLdapClient,
                                    ConfirmationTokenService confirmationTokenService, EmailSenderService emailSenderService, ColorConsoleOutput console) {
        this.authenticationManager = authenticationManager;
        this.userLdapClient = userLdapClient;
        this.confirmationTokenService = confirmationTokenService;
        this.emailSenderService = emailSenderService;
        this.console = console;
    }

    @RequestMapping(method = POST)
    public ResponseEntity<?> authenticationRequest(@RequestBody AuthenticationRequest authenticationRequest)
        throws AuthenticationException, JOSEException {

        final String username = authenticationRequest.getUsername();
        final String password = authenticationRequest.getPassword();

        UserLdap userFound = userLdapClient.findById(username);
        if (userFound != null) {
            assertAccountEnabled(userFound);

            UsernamePasswordAuthenticationToken userAuthToken = new UsernamePasswordAuthenticationToken(username, password);
            // throws authenticationException if it fails !
            Authentication authentication = authenticationManager.authenticate(userAuthToken);
            if (userAuthToken.isAuthenticated()) {
                SecurityContextHolder.getContext().setAuthentication(userAuthToken);
                log.debug(String.format("Auto login %s successfully!", username));
            }

            final String token = generateSignedJWTToken(username, authentication);

            //log.info(console.classMsg("LDAP enabled: ") + Boolean.parseBoolean(ldapEnabled));
            //String test = userService.getUserDetail(username);

            // Token sample
                /*  {
                    "alg":"HS256",
                    "typ":"JWT"
                }
                {
                    "username": "user",
                    "role": "admin",
                    "iat": 1556172533,
                    "exp": 1556173133
                }
                HMACSHA256(
                    base64UrlEncode(header) + "." +
                    base64UrlEncode(payload),
                    SECRET!
                ) */

            // Return the token
            return ResponseEntity.ok(new AuthenticationResponse(token));

        } else {
            log.info(console.classMsg(getClass().getSimpleName(), " User not exists: ") + blTxt(String.valueOf(userFound)));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("User doesn't exists on the server");
        }
    }

    @RequestMapping(value = "/social", method = POST)
    public ResponseEntity<?> socialLogin(@RequestBody SocialAuthRequest socialAuthRequest) throws JOSEException {

        final String id = socialAuthRequest.getId();
        final String provider = socialAuthRequest.getSocialProvider();

        final UserLdap userFound = userLdapClient.getUserBySocialId(id, provider);
        if (userFound != null) {
            assertAccountEnabled(userFound);

            final String username = userFound.getUid();
            String authorities = userLdapClient.getAuthoritiesMembershipById(username);
            final String token = generateSignedJWTToken(username, authorities);

            // Return the token
            return ResponseEntity.ok(new SocialAuthResponse(token, username));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Account associated with this credentials not found");
        }
    }

    private void assertAccountEnabled(UserLdap user) {
        if (user.getAccountState() != AccountState.ENABLED) {
            if (user.getAccountState() == AccountState.WAITING_CONFIRMATION) {
                log.info(console.classMsg(getClass().getSimpleName(), " Account not confirmed: ") + blTxt(String.valueOf(user)));
                throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Confirm registration. There should be a letter in the mail with a confirmation link.");
            } else if (user.getAccountState() == AccountState.DISABLED) {
                log.info(console.classMsg(getClass().getSimpleName(), " Account disabled: ") + blTxt(String.valueOf(user)));
                throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "User with this email disabled on the server. Contact with administrator.");
            }
        }
    }

    private String generateSignedJWTToken(String username, Authentication authentication) throws JOSEException {
        return generateHMACToken(username, authentication.getAuthorities(), secret, expirationInMinutes);
    }

    private String generateSignedJWTToken(String username, String roles) throws JOSEException {
        return generateHMACToken(username, roles, secret, expirationInMinutes);
    }
}


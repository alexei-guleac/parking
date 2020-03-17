package com.isd.parking.controller.web;

import com.isd.parking.models.User;
import com.isd.parking.security.model.payload.AuthenticationRequest;
import com.isd.parking.security.model.payload.AuthenticationResponse;
import com.isd.parking.security.model.payload.SocialAuthRequest;
import com.isd.parking.security.model.payload.SocialAuthResponse;
import com.isd.parking.service.ConfirmationTokenService;
import com.isd.parking.service.EmailSenderService;
import com.isd.parking.service.ldap.UserLdapClient;
import com.isd.parking.utils.ColorConsoleOutput;
import com.isd.parking.utils.FileUtils;
import com.nimbusds.jose.JOSEException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.io.IOException;
import java.util.List;

import static com.isd.parking.security.JwtUtils.generateHMACToken;
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

    private final String secret = new FileUtils().getResourceAsString(secretKeyFile);

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
            throws AuthenticationException, IOException, JOSEException {

        final String username = authenticationRequest.getUsername();
        final String password = authenticationRequest.getPassword();

        UsernamePasswordAuthenticationToken userAuthToken = new UsernamePasswordAuthenticationToken(username, password);
        // throws authenticationException if it fails !
        Authentication authentication = this.authenticationManager.authenticate(userAuthToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

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
    }

    @RequestMapping(value = "/social", method = POST)
    public ResponseEntity<?> socialLogin(@RequestBody SocialAuthRequest socialAuthRequest) throws JOSEException {

        final String id = socialAuthRequest.getId();
        final String provider = socialAuthRequest.getSocialProvider();

        final List<User> userFound = userLdapClient.getUsersBySocialId(id, provider);
        if (!userFound.isEmpty()) {
            final String username = userFound.get(0).getUsername();
            List<String> authorities = userLdapClient.getAuthoritiesMembershipById(username);

            final String token = generateSignedJWTToken(username, authorities.get(0));

            // Return the token
            return ResponseEntity.ok(new SocialAuthResponse(token, username));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Account associated with this credentials not found");
        }
    }

    private String generateSignedJWTToken(String username, Authentication authentication) throws JOSEException {
        return generateHMACToken(username, authentication.getAuthorities(), secret, expirationInMinutes);
    }

    private String generateSignedJWTToken(String username, String roles) throws JOSEException {
        return generateHMACToken(username, roles, secret, expirationInMinutes);
    }
}


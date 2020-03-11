package com.isd.parking.controller.web;

import com.isd.parking.security.model.AuthenticationRequest;
import com.isd.parking.security.model.AuthenticationResponse;
import com.nimbusds.jose.JOSEException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.nio.charset.Charset;
import java.util.Objects;

import static com.isd.parking.security.JwtUtils.generateHMACToken;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("auth")
@Slf4j
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @RequestMapping(method = POST)
    public ResponseEntity<?> authenticationRequest(@RequestBody AuthenticationRequest authenticationRequest)
            throws AuthenticationException, IOException, JOSEException {

        log.info("authenticationRequest");
        log.info(String.valueOf(authenticationRequest));

        String username = authenticationRequest.getUsername();
        String password = authenticationRequest.getPassword();
        log.info(username + " " + password);

        UsernamePasswordAuthenticationToken t = new UsernamePasswordAuthenticationToken(username, password);
        log.info(String.valueOf(t));

        // throws authenticationException if it fails !
        Authentication authentication = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        log.info(String.valueOf("after " + authentication));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String secret = IOUtils.toString(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("secret.key")), Charset.defaultCharset());
        int expirationInMinutes = 24 * 60;

        String token = generateHMACToken(username, authentication.getAuthorities(), secret, expirationInMinutes);

        // Return the token
        return ResponseEntity.ok(new AuthenticationResponse(token));
    }
}

package com.isd.parking.config.security;

import com.isd.parking.utils.ColorConsoleOutput;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Component
@Slf4j
public class CustomPasswordEncoder implements PasswordEncoder {

    private final String salt = "$2a$12$lIGeCCVi1fkIYIZA9ly6ge";

    private final ColorConsoleOutput console;

    @Autowired
    public CustomPasswordEncoder(ColorConsoleOutput console) {
        this.console = console;
    }

    @Override
    public String encode(CharSequence rawPassword) {
        log.info(console.methodMsg("in custom bc encoder"));
        return "{customBC}" + BCrypt.hashpw(rawPassword.toString(), salt);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        log.info(console.methodMsg("in custom bc encoder"));
        return BCrypt.checkpw(rawPassword.toString(), encodedPassword.substring(10));
    }

    public String getH(String s) {
        return this.encode(s);
    }

    /**
     * Generates SHA encrypted string from users password
     *
     * @param password - user password to be encrypted
     * @return SHA encrypted string from users password
     */
    public static String digestSHA(final String password) {
        String base64;

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA");
            digest.update(password.getBytes());
            base64 = Base64.getEncoder()
                    .encodeToString(digest.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        return "{SHA}" + base64;
    }
}


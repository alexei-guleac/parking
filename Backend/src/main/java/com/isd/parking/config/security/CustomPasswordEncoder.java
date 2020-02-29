package com.isd.parking.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Component
@Slf4j
public class CustomPasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence rawPassword) {
        //log.info("in custom bc enc {encode}");
        final String salt = "$2a$12$lIGeCCVi1fkIYIZA9ly6ge";
        return "{customBC}" + BCrypt.hashpw(rawPassword.toString(), salt);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        //log.info("in custom bc enc {matches}");
        return BCrypt.checkpw(rawPassword.toString(), encodedPassword.substring(10));
    }

    public String getH(String s) {
        return this.encode(s);
    }

    /*public String encode(String str) {
        StringBuilder sb = new StringBuilder();
        char tmp = str.charAt(0);
        int count = 1;
        for (int idx = 1; idx < str.length(); idx++) {
            char curr = str.charAt(idx);
            if (curr == tmp) {
                count++;
            } else {
                sb.append(tmp).append(count);
                count = 1;
            }
            tmp = curr;
        }
        sb.append(tmp).append(count);
        return sb.toString();
    }

    public String decode(String str) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i += 2) {
            int count = Integer.parseInt("" + str.charAt(i + 1));
            for (int j = 0; j < count; j++) {
                sb.append(str.charAt(i));
            }
        }
        return sb.toString();
    }*/

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


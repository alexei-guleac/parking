package com.isd.parking.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;


/**
 * Contains password encoding methods and beans
 */
@Configuration
public class PasswordEncoding {

    /**
     * Standard Spring BCrypt password encoder bean
     *
     * @return BCrypt password encoder
     */
    @Bean
    public static BCryptPasswordEncoder bcryptEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Custom Spring BCrypt password encoder bean with encoding type mark
     *
     * @return BCrypt password encoder with mark
     */
    @Bean
    public static BCryptPasswordEncoder passwordEncoderBc() {
        final BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
        return new BCryptPasswordEncoder() {

            @Override
            public String encode(CharSequence rawPassword) {
                return "{bcrypt}" + bcrypt.encode(rawPassword.toString());
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return bcrypt.matches(rawPassword, encodedPassword.substring(8));
            }
        };
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

    /**
     * Gets default application password encoder
     *
     * @return default password encoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return DefaultPasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    static class DefaultPasswordEncoderFactories {

        /**
         * Creates collection of different types of password encoders for further selection with setted default encoder
         *
         * @return default password encoder
         */
        static PasswordEncoder createDelegatingPasswordEncoder() {
            String encodingId = "bcrypt";
            Map<String, PasswordEncoder> encoders = new HashMap<>();
            encoders.put(encodingId, bcryptEncoder());
            encoders.put("ldap", new org.springframework.security.crypto.password.LdapShaPasswordEncoder());
            encoders.put("MD4", new org.springframework.security.crypto.password.Md4PasswordEncoder());
            encoders.put("MD5", new org.springframework.security.crypto.password.MessageDigestPasswordEncoder("MD5"));
            encoders.put("noop", org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance());
            encoders.put("pbkdf2", new Pbkdf2PasswordEncoder());
            encoders.put("scrypt", new SCryptPasswordEncoder());
            encoders.put("SHA-1", new org.springframework.security.crypto.password.MessageDigestPasswordEncoder("SHA-1"));
            encoders.put("SHA-256", new org.springframework.security.crypto.password.MessageDigestPasswordEncoder("SHA-256"));
            encoders.put("sha256", new org.springframework.security.crypto.password.StandardPasswordEncoder());
            encoders.put("customBC", new CustomBcryptPasswordEncoder());

            DelegatingPasswordEncoder delegatingPasswordEncoder = new DelegatingPasswordEncoder(encodingId, encoders);
            delegatingPasswordEncoder.setDefaultPasswordEncoderForMatches(encoders.getOrDefault("customBC", new CustomBcryptPasswordEncoder()));

            return delegatingPasswordEncoder;
        }
    }

    /**
     * Custom Spring BCrypt password encoder bean with encoding type mark and fixed salt
     *
     * @return BCrypt password encoder with mark and fixed salt
     */
    @Component
    public static class CustomBcryptPasswordEncoder implements PasswordEncoder {

        private final String salt = "$2a$12$lIGeCCVi1fkIYIZA9ly6ge";

        @Override
        public String encode(CharSequence rawPassword) {
            return "{customBC}" + BCrypt.hashpw(rawPassword.toString(), salt);
        }

        @Override
        public boolean matches(CharSequence rawPassword, String encodedPassword) {
            return BCrypt.checkpw(rawPassword.toString(), encodedPassword.substring(10));
        }

        public String getH(String s) {
            return this.encode(s);
        }
    }
}

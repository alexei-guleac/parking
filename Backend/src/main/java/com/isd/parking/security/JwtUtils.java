package com.isd.parking.security;


import com.isd.parking.utilities.AppFileUtils;
import com.isd.parking.web.rest.errors.exceptions.JwtBadSignatureException;
import com.isd.parking.web.rest.errors.exceptions.JwtExpirationException;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jwt.util.DateUtils;
import lombok.val;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.Set;


/**
 * Utilities methods for JWT processing
 */
@Component
public final class JwtUtils {

    private final static String AUDIENCE_UNKNOWN = "unknown";

    private final static String AUDIENCE_WEB = "web";

    private final static String AUDIENCE_MOBILE = "mobile";

    private final static String AUDIENCE_TABLET = "tablet";

    private final static String ROLES_CLAIM = "roles";

    private final String secretKeyFile = "secret.key";

    private final String secret = new AppFileUtils().getResourceAsString(secretKeyFile);

    // JWT token expiration period in minutes (by default 3 days)
    private final int expirationInMinutes = 72 * 60;

    /**
     * Method generates JWT based on target user granted authorities list and API secret key
     *
     * @param subject-             target user for token claiming
     * @param roles-               user granted authorities
     * @param secret-              secret key for signing token
     * @param expirationInMinutes- token expiration in minutes
     * @return serialized signed JWT
     * @throws JOSEException
     */
    public static String generateHMACToken(String subject,
                                           @NotNull Collection<? extends GrantedAuthority> roles,
                                           @NotNull String secret,
                                           int expirationInMinutes) throws JOSEException {
        return generateHMACToken(subject,
            AuthorityListToCommaSeparatedString(roles), secret, expirationInMinutes);
    }

    /**
     * Method generates JWT based on target user roles and API secret key
     *
     * @param subject             - target user for token claiming
     * @param roles               - user roles (granted authorities)
     * @param secret              - secret key for signing token
     * @param expirationInMinutes - token expiration in minutes
     * @return serialized signed JWT
     * @throws JOSEException - if provided corrupted secret sign
     */
    public static String generateHMACToken(String subject, String roles,
                                           @NotNull String secret, int expirationInMinutes)
        throws JOSEException {
        @NotNull JWSSigner signer = new MACSigner(secret);
        JWTClaimsSet payload = new JWTClaimsSet.Builder()
            .subject(subject)
            .issueTime(currentDate())
            .expirationTime(expirationDate(expirationInMinutes))
            .claim(ROLES_CLAIM, roles)
            .audience(AUDIENCE_WEB)
            .build();

        val header = new JWSHeader.Builder(JWSAlgorithm.HS256)
            .type(JOSEObjectType.JWT)
            .build();

        @NotNull SignedJWT signedJWT = new SignedJWT(header, payload);
        signedJWT.sign(signer);
        return signedJWT.serialize();
    }

    private static @NotNull Date currentDate() {
        return new Date(System.currentTimeMillis());
    }

    /**
     * Method creates expiration date based on specified expiration period
     *
     * @param expirationInMinutes - specified token expiration period
     * @return date when token expires
     */
    private static @NotNull Date expirationDate(int expirationInMinutes) {
        return new Date(System.currentTimeMillis() + expirationInMinutes * 60 * 1000);
    }

    /**
     * Assert that token not expired
     *
     * @param jwt - target JWT
     * @throws ParseException
     */
    public static void assertNotExpired(@NotNull SignedJWT jwt) throws ParseException {
        if (DateUtils.isBefore(jwt.getJWTClaimsSet().getExpirationTime(), currentDate(), 60)) {
            throw new JwtExpirationException(
                "Token has expired");
        }
    }

    /**
     * Validates token signature
     *
     * @param jwt-   target JWT
     * @param secret - secret key for signing token
     * @throws JOSEException
     */
    public static void assertValidSignature(@NotNull SignedJWT jwt, @NotNull String secret) throws JOSEException {
        if (!verifyHMACToken(jwt, secret)) {
            throw new JwtBadSignatureException("Signature is not valid");
        }
    }

    public static @NotNull SignedJWT parse(@NotNull String token) throws ParseException {
        return SignedJWT.parse(token);
    }

    private static boolean verifyHMACToken(@NotNull SignedJWT jwt, @NotNull String secret) throws JOSEException {
        @NotNull JWSVerifier verifier = new MACVerifier(secret);
        return jwt.verify(verifier);
    }

    /**
     * Сonverts user granted authorities collection to comma separated string
     *
     * @param authorities - target user granted authorities collection
     * @return comma separated string value of user's authority list
     */
    private static String AuthorityListToCommaSeparatedString(@NotNull Collection<? extends GrantedAuthority> authorities) {
        Set<String> authoritiesAsSetOfString = AuthorityUtils.authorityListToSet(authorities);
        return StringUtils.join(authoritiesAsSetOfString, ", ");
    }

    /**
     * Get user name from target JWT
     *
     * @param jwt - target JWT
     * @return username
     * @throws ParseException
     */
    public static String getUsername(@NotNull SignedJWT jwt) throws ParseException {
        return jwt.getJWTClaimsSet().getSubject();
    }

    /**
     * Retrieves user granted authorities from JWT roles field
     *
     * @param jwt - target JWT
     * @return granted authorities collection
     * @throws ParseException
     */
    public static Collection<? extends GrantedAuthority> getRoles(@NotNull SignedJWT jwt) throws ParseException {
        Collection<? extends GrantedAuthority> authorities;
        String roles = jwt.getJWTClaimsSet().getStringClaim(ROLES_CLAIM);
        authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(roles);

        return authorities;
    }

    /**
     * Retrieves issue date from JWT
     *
     * @param jwt - target JWT
     * @return issue date
     * @throws ParseException
     */
    public static Date getIssueTime(@NotNull SignedJWT jwt) throws ParseException {
        return jwt.getJWTClaimsSet().getIssueTime();
    }

    @Bean
    public JwtUtils getJwtUtils() {
        return new JwtUtils();
    }

    /**
     * Generates signed JWT Token from Authentication object
     *
     * @param username       - user id
     * @param authentication - Authentication object
     * @return signed JWT Token
     * @throws JOSEException
     */
    public String generateSignedJWTToken(String username, @NotNull Authentication authentication) throws JOSEException {
        return generateHMACToken(username, authentication.getAuthorities(), secret, expirationInMinutes);
    }

    /**
     * Generates signed JWT Token with specified roles
     *
     * @param username - user id
     * @param roles    - user roles String
     * @return signed JWT Token
     * @throws JOSEException
     */
    public String generateSignedJWTToken(String username, String roles) throws JOSEException {
        return generateHMACToken(username, roles, secret, expirationInMinutes);
    }

}

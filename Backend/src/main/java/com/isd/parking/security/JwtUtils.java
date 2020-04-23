package com.isd.parking.security;


import com.isd.parking.security.exceptions.JwtBadSignatureException;
import com.isd.parking.security.exceptions.JwtExpirationException;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jwt.util.DateUtils;
import lombok.val;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.Set;


/**
 * Utilities methods for JWT processing
 */
public final class JwtUtils {

    private final static String AUDIENCE_UNKNOWN = "unknown";

    private final static String AUDIENCE_WEB = "web";

    private final static String AUDIENCE_MOBILE = "mobile";

    private final static String AUDIENCE_TABLET = "tablet";

    private final static String ROLES_CLAIM = "roles";

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
    public static String generateHMACToken(String subject, Collection<? extends GrantedAuthority> roles,
                                           String secret, int expirationInMinutes) throws JOSEException {
        return generateHMACToken(subject, AuthorityListToCommaSeparatedString(roles), secret, expirationInMinutes);
    }

    /**
     * Method generates JWT based on target user roles and API secret key
     *
     * @param subject             - target user for token claiming
     * @param roles               - user roles (granted authorities)
     * @param secret              - secret key for signing token
     * @param expirationInMinutes - token expiration in minutes
     * @return serialized signed JWT
     * @throws JOSEException
     */
    public static String generateHMACToken(String subject, String roles, String secret, int expirationInMinutes)
        throws JOSEException {
        JWSSigner signer = new MACSigner(secret);
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

        SignedJWT signedJWT = new SignedJWT(header, payload);
        signedJWT.sign(signer);
        return signedJWT.serialize();
    }

    private static Date currentDate() {
        return new Date(System.currentTimeMillis());
    }

    /**
     * Method creates expiration date based on specified expiration period
     *
     * @param expirationInMinutes - specified token expiration period
     * @return date when token expires
     */
    private static Date expirationDate(int expirationInMinutes) {
        return new Date(System.currentTimeMillis() + expirationInMinutes * 60 * 1000);
    }

    /**
     * Assert that token not expired
     *
     * @param jwt - target JWT
     * @throws ParseException
     */
    public static void assertNotExpired(SignedJWT jwt) throws ParseException {
        if (DateUtils.isBefore(jwt.getJWTClaimsSet().getExpirationTime(), currentDate(), 60)) {
            throw new JwtExpirationException("Token has expired");
        }
    }

    /**
     * Validates token signature
     *
     * @param jwt-   target JWT
     * @param secret - secret key for signing token
     * @throws JOSEException
     */
    public static void assertValidSignature(SignedJWT jwt, String secret) throws JOSEException {
        if (!verifyHMACToken(jwt, secret)) {
            throw new JwtBadSignatureException("Signature is not valid");
        }
    }

    public static SignedJWT parse(String token) throws ParseException {
        return SignedJWT.parse(token);
    }

    private static boolean verifyHMACToken(SignedJWT jwt, String secret) throws JOSEException {
        JWSVerifier verifier = new MACVerifier(secret);
        return jwt.verify(verifier);
    }

    /**
     * Сonverts user granted authorities collection to comma separated string
     *
     * @param authorities - target user granted authorities collection
     * @return comma separated string value of user's authority list
     */
    private static String AuthorityListToCommaSeparatedString(Collection<? extends GrantedAuthority> authorities) {
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
    public static String getUsername(SignedJWT jwt) throws ParseException {
        return jwt.getJWTClaimsSet().getSubject();
    }

    /**
     * Retrieves user granted authorities from JWT roles field
     *
     * @param jwt - target JWT
     * @return granted authorities collection
     * @throws ParseException
     */
    public static Collection<? extends GrantedAuthority> getRoles(SignedJWT jwt) throws ParseException {
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
    public static Date getIssueTime(SignedJWT jwt) throws ParseException {
        return jwt.getJWTClaimsSet().getIssueTime();
    }

}

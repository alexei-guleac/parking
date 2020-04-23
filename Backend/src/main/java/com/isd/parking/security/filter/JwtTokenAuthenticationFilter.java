package com.isd.parking.security.filter;


import com.isd.parking.security.JwtUtils;
import com.isd.parking.security.exceptions.JwtBadSignatureException;
import com.isd.parking.security.exceptions.JwtExpirationException;
import com.isd.parking.security.exceptions.MalformedJwtException;
import com.isd.parking.security.model.JwtUser;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.Collection;
import java.util.Date;

import static com.isd.parking.security.JwtUtils.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;


/**
 * Jwt Token Authentication Filter for filter authentication requests
 */
@Slf4j
public class JwtTokenAuthenticationFilter extends GenericFilterBean {

    private final RequestMatcher requestMatcher;

    private final String secretKey;

    public JwtTokenAuthenticationFilter(String path, String secretKey) {
        this.requestMatcher = new AntPathRequestMatcher(path);
        this.secretKey = secretKey;
    }

    /**
     * Method filters HTTP request to get authorization information and set access to specified resources
     *
     * @param req   - HTTP Java Servlet request
     * @param res   - HTTP Java Servlet response
     * @param chain - built-in Spring Framework HTTP filter chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        if (!requiresAuthentication(request)) {
            /*
            if the URL requested doesn't match the URL handled by the filter, then we chain to the next filters.
             */
            chain.doFilter(request, response);
            return;
        }

        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            /*
            If there's not authentication information, then we chain to the next filters.
             The SecurityContext will be analyzed by the chained filter that will throw AuthenticationExceptions if necessary
            */
            chain.doFilter(request, response);
            return;
        }

        try {
            /*
            The token is extracted from the header. It's then checked (signature and expiration)
            An Authentication is then created and registered in the SecurityContext.
            The SecurityContext will be analyzed by chained filters that will throw Exceptions if necessary
            (like if authorizations are incorrect).
            */
            SignedJWT jwt = extractAndDecodeJwt(request);
            checkAuthenticationAndValidity(jwt);
            Authentication auth = buildAuthenticationFromJwt(jwt, request);
            SecurityContextHolder.getContext().setAuthentication(auth);

            chain.doFilter(request, response);

        } catch (JwtExpirationException ex) {
            throw new AccountExpiredException("Account authorization JWT token is not valid anymore ");
        } catch (JwtBadSignatureException | ParseException | JOSEException ex) {
            throw new MalformedJwtException("Authorization JWT token is malformed");
        }

        /* SecurityContext is then cleared since we are stateless.*/
        SecurityContextHolder.clearContext();
    }

    private boolean requiresAuthentication(HttpServletRequest request) {
        return requestMatcher.matches(request);
    }

    /**
     * Extracts token from HTTP request headers
     *
     * @param request - target request
     * @return signed JWT token
     * @throws ParseException
     */
    private SignedJWT extractAndDecodeJwt(HttpServletRequest request) throws ParseException {
        String authHeader = request.getHeader(AUTHORIZATION);
        String token = authHeader.substring("Bearer ".length());

        return parse(token);
    }

    /**
     * Check that JWT token is not expired and signature secret key conforms stored
     *
     * @param jwt - specified JWT
     * @throws ParseException
     * @throws JOSEException
     */
    private void checkAuthenticationAndValidity(SignedJWT jwt) throws ParseException, JOSEException {
        assertNotExpired(jwt);
        assertValidSignature(jwt, secretKey);
    }

    /**
     * Method build Spring Authentication object based on JWT token
     *
     * @param jwt     - specified signed JWT token
     * @param request - target HTTP request
     * @return Authentication object result
     * @throws ParseException
     */
    private Authentication buildAuthenticationFromJwt(SignedJWT jwt, HttpServletRequest request) throws ParseException {

        String username = getUsername(jwt);
        Collection<? extends GrantedAuthority> authorities = JwtUtils.getRoles(jwt);
        Date creationDate = getIssueTime(jwt);
        JwtUser userDetails = new JwtUser(username, creationDate, authorities);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        return authentication;
    }
}

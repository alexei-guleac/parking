package com.isd.parking.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isd.parking.models.ErrorMessage;
import com.isd.parking.models.ResponseWrapper;
import com.isd.parking.models.RestErrorList;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;

import static java.util.Collections.singletonMap;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


/**
 * SecurityAuthenticationEntryPoint is called by ExceptionTranslationFilter to handle all AuthenticationException.
 * These exceptions are thrown when authentication failed : wrong login/password, authentication unavailable, invalid token
 * authentication expired, etc.
 * For problems related to access (roles), see RestAccessDeniedHandler.
 */
public class SecurityAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         @NotNull HttpServletResponse response,
                         @NotNull AuthenticationException authException) throws IOException {

        @NotNull RestErrorList errorList = new RestErrorList(
            SC_UNAUTHORIZED, new ErrorMessage(authException.getMessage()));
        @NotNull ResponseWrapper responseWrapper = new ResponseWrapper(
            null, singletonMap("status", SC_UNAUTHORIZED), errorList);
        @NotNull ObjectMapper objMapper = new ObjectMapper();

        @NotNull HttpServletResponseWrapper wrapper = new HttpServletResponseWrapper(response);
        wrapper.setStatus(SC_UNAUTHORIZED);
        wrapper.setContentType(APPLICATION_JSON_VALUE);
        wrapper.getWriter().println(objMapper.writeValueAsString(responseWrapper));
        wrapper.getWriter().flush();
    }
}

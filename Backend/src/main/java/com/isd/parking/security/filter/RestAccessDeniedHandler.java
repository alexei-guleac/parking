package com.isd.parking.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isd.parking.models.ErrorMessage;
import com.isd.parking.models.ResponseWrapper;
import com.isd.parking.models.RestErrorList;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;

import static java.util.Collections.singletonMap;
import static org.apache.commons.httpclient.HttpStatus.SC_FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


/**
 * The RestAccessDeniedHandler is called by the ExceptionTranslationFilter to handle all AccessDeniedExceptions.
 * These exceptions are thrown when the authentication is valid but access is not authorized.
 */
public class RestAccessDeniedHandler implements AccessDeniedHandler {


    @Override
    public void handle(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull AccessDeniedException accessDeniedException)
        throws IOException {
        @NotNull RestErrorList errorList = new RestErrorList(
            SC_FORBIDDEN, new ErrorMessage(accessDeniedException.getMessage()));
        @NotNull ResponseWrapper responseWrapper = new ResponseWrapper(
            null, singletonMap("status", SC_FORBIDDEN), errorList);
        @NotNull ObjectMapper objMapper = new ObjectMapper();

        final @NotNull HttpServletResponseWrapper wrapper = new HttpServletResponseWrapper(response);
        wrapper.setStatus(SC_FORBIDDEN);
        wrapper.setContentType(APPLICATION_JSON_VALUE);
        wrapper.getWriter().println(objMapper.writeValueAsString(responseWrapper));
        wrapper.getWriter().flush();
    }
}

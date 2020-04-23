package com.isd.parking.security.config.cookie;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * Custom cookie filter for fix CORS same site issue
 */
@Component
public class CookieFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {

        HttpServletResponse resp = (HttpServletResponse) response;
        resp.setHeader("Set-Cookie", "locale=de; HttpOnly; SameSite=strict");
        chain.doFilter(request, response);
    }
}

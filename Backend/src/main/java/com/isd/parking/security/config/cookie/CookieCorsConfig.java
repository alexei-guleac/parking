package com.isd.parking.security.config.cookie;

import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.handler.MappedInterceptor;


/**
 * CORS cookie config for fix CORS same site issue
 */
@Configuration
public class CookieCorsConfig {

    private final CookieServiceInterceptor cookieServiceInterceptor;

    @Autowired
    public CookieCorsConfig(CookieServiceInterceptor cookieServiceInterceptor) {
        this.cookieServiceInterceptor = cookieServiceInterceptor;
    }

    @Bean
    public @Nullable MappedInterceptor myInterceptor() {
        return new MappedInterceptor(null, cookieServiceInterceptor);
    }
}

package com.isd.parking.config.security;

import com.isd.parking.utils.ColorConsoleOutput;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${ldap.url}")
    private String ldapUrl;

    @Value("${spring.ldap.embedded.ldif}")
    private String ldapFile;

    @Value("${spring.ldap.embedded.port}")
    private String ldapPort;

    @Value("${ldap.partitionSuffix}")
    private String ldapPartitionSuffix;

    @Value("${ldap.user.dn.pattern}")
    private String ldapUserDnPattern;

    @Value("${spring.ldap.base}")
    private String ldapSearchBase;

    @Value("${ldap.userSearchFilter}")
    private String ldapUserSearchFilter;

    @Value("${ldap.groupSearchBase}")
    private String ldapGroupSearchBase;

    @Value("${ldap.groupSearchFilter}")
    private String ldapGroupSearchFilter;

    @Value("${ldap.passwordAttribute}")
    private String ldapPasswordAttribute;

    private final String[] pathArray = new String[]{"/login", "/registration", "/validate_captcha", "/parking", "/arduino", "/demo"};

    // ----------  LDAP auth --------------

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(new CORSFilter(), ChannelProcessingFilter.class);
        http.cors().and()
                // We don't need CSRF for this cause
                .csrf().disable()
                .headers()
                .frameOptions().sameOrigin().and()
                .authorizeRequests()
                // dont authenticate this particular request
                .antMatchers(pathArray).permitAll()
                // all other requests need to be authenticated
                .anyRequest().fullyAuthenticated()
                .and().exceptionHandling()
                .and().httpBasic();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .ldapAuthentication()
                .userDnPatterns(ldapUserDnPattern)
                .userSearchBase(ldapSearchBase)
                .userSearchFilter(ldapUserSearchFilter)
                .groupSearchBase(ldapGroupSearchBase)
                .groupSearchFilter(ldapGroupSearchFilter)
                .contextSource()
                .ldif(ldapFile)
                .url(ldapUrl + "/" + ldapPartitionSuffix)
                .port(Integer.parseInt(ldapPort))
                .root(ldapPartitionSuffix)
                .and()
                .passwordCompare()
                .passwordEncoder(new CustomPasswordEncoder(new ColorConsoleOutput()))
                .passwordAttribute(ldapPasswordAttribute);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return DefaultPasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    static class DefaultPasswordEncoderFactories {
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
            encoders.put("customBC", new CustomPasswordEncoder(new ColorConsoleOutput()));

            DelegatingPasswordEncoder delegatingPasswordEncoder = new DelegatingPasswordEncoder(encodingId, encoders);
            delegatingPasswordEncoder.setDefaultPasswordEncoderForMatches(encoders.getOrDefault("customBC", new CustomPasswordEncoder(new ColorConsoleOutput())));

            return delegatingPasswordEncoder;
        }
    }

    @Bean
    public static BCryptPasswordEncoder bcryptEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public static BCryptPasswordEncoder passwordEncoderBc() {
        final BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
        return new BCryptPasswordEncoder() {

            @Autowired
            private ColorConsoleOutput console;

            @Override
            public String encode(CharSequence rawPassword) {
                log.info(console.methodMsg("in ws config bc"));
                return "{bcrypt}" + bcrypt.encode(rawPassword.toString());
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                log.info(console.methodMsg("in ws config bc"));
                return bcrypt.matches(rawPassword, encodedPassword.substring(8));
            }
        };
    }

    @Component
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public static class CORSFilter implements Filter {

        @Value("${front.url}")
        private String frontUrl;

        /**
         * CORS filter for http-request and response
         */
        public CORSFilter() {
            super();
        }

        /**
         * Do Filter on every http-request.
         */
        @Override
        public final void doFilter(final ServletRequest req, final ServletResponse res, final FilterChain chain) throws IOException, ServletException {
            final HttpServletResponse response = (HttpServletResponse) res;
            response.setHeader("Access-Control-Allow-Origin", frontUrl);

            // without this header jquery.ajax calls returns 401 even after successful login and SSESSIONID being succesfully stored.
            response.setHeader("Access-Control-Allow-Credentials", "true");

            response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
            response.setHeader("Access-Control-Max-Age", "3600");
            response.setHeader("Access-Control-Allow-Headers", "X-Requested-With, Authorization, Origin, Content-Type, Version");
            response.setHeader("Access-Control-Expose-Headers", "X-Requested-With, Authorization, Origin, Content-Type");

            final HttpServletRequest request = (HttpServletRequest) req;
            if (!request.getMethod().equals("OPTIONS")) {
                chain.doFilter(req, res);
            } else {
                response.setStatus(HttpServletResponse.SC_OK);
            }
        }

        /**
         * Destroy method
         */
        @Override
        public void destroy() {
        }

        /**
         * Initialize CORS filter
         */
        @Override
        public void init(FilterConfig arg0) throws ServletException {
        }
    }
}

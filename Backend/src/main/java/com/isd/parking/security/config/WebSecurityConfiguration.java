package com.isd.parking.security.config;

import com.isd.parking.security.PasswordEncoding.CustomBcryptPasswordEncoder;
import com.isd.parking.security.filter.JwtTokenAuthenticationFilter;
import com.isd.parking.security.filter.RestAccessDeniedHandler;
import com.isd.parking.security.filter.SecurityAuthenticationEntryPoint;
import com.isd.parking.utils.AppFileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;


@Configuration
@Slf4j
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final String secretKeyFile = "secret.key";
    private final LdapAuthoritiesPopulator ldapAuthoritiesPopulator;
    private final String[] pathArray = new String[]{
        "/auth/**", "/login", "/login/**", "/registration",
        "/validate_captcha", "/confirm_account",
        "/forgot-password", "/reset-password",
        "/parking", "/arduino", "/demo"
    };
    @Value("${spring.data.ldap.enabled}")
    private boolean ldapEnabled;
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
    @Value("${ldap.url}")
    private String ldapProviderUrl;
    @Value("${spring.inmemoryauth.user}")
    private String usernameInMemory;
    @Value("${spring.inmemoryauth.userpassword}")
    private String userPasswordInMemory;
    @Value("${spring.inmemoryauth.admin}")
    private String adminInMemory;
    @Value("${spring.inmemoryauth.adminpassword}")
    private String adminPasswordInMemory;

    @Autowired
    public WebSecurityConfiguration(LdapAuthoritiesPopulator ldapAuthoritiesPopulator) {
        /*
         Ignores the default configuration, useless in our case (session management, etc..)
        */
        super(true);
        this.ldapAuthoritiesPopulator = ldapAuthoritiesPopulator;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        /* the secret key used to sign the JWT token is known exclusively by the server.
         With Nimbus JOSE implementation, it must be at least 256 characters longs.
         */
        String secret = new AppFileUtils().getResourceAsString(secretKeyFile);

        http.addFilterBefore(new CORSFilter(), ChannelProcessingFilter.class);
        http.
            cors()
            .and()
            // We don't need CSRF for this cause
            .csrf().disable()
            .headers()
            .frameOptions().sameOrigin()
            .and()
            /*
            Filters are added just after the ExceptionTranslationFilter so that Exceptions are catch by the exceptionHandling()
             Further information about the order of filters, see FilterComparator
             */
            .addFilterAfter(jwtTokenAuthenticationFilter("/**", secret), ExceptionTranslationFilter.class)
            /*
             Exception management is handled by the authenticationEntryPoint (for exceptions related to authentications)
             and by the AccessDeniedHandler (for exceptions related to access rights)
            */
            .exceptionHandling()
            .authenticationEntryPoint(new SecurityAuthenticationEntryPoint())
            .accessDeniedHandler(new RestAccessDeniedHandler())
            .and()
            /*
              anonymous() consider no authentication as being anonymous instead of null in the security context.
             */
            .anonymous()
            .and()
            /* No Http session is used to get the security context */
            .sessionManagement().sessionCreationPolicy(STATELESS)
            .and()
            .authorizeRequests()
            // dont authenticate this particular request
            .antMatchers(pathArray).permitAll()
            // all other requests need to be authenticated
            .anyRequest().fullyAuthenticated()
            .and()
            .httpBasic();
    }

    // ----------  LDAP auth --------------

    /**
     * Configure AuthenticationManagerBuilder to use the specified DetailsService.
     *
     * @param auth the {@link AuthenticationManagerBuilder} à utiliser
     * @throws Exception
     */
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        /*
            see this article : https://spring.io/guides/gs/authenticating-ldap/
            We  redefine our own LdapAuthoritiesPopulator which need ContextSource().
            We need to delegate the creation of the contextSource out of the builder-configuration.
        */

        if (ldapEnabled) {
            auth
                .ldapAuthentication()
                .userDnPatterns(ldapUserDnPattern)
                .userSearchBase(ldapSearchBase)
                .userSearchFilter(ldapUserSearchFilter)
                .groupSearchBase(ldapGroupSearchBase)
                .groupSearchFilter(ldapGroupSearchFilter)
                .ldapAuthoritiesPopulator(ldapAuthoritiesPopulator)
                .contextSource()
                .ldif(ldapFile)
                .url(ldapProviderUrl + "/" + ldapPartitionSuffix)
                .port(Integer.parseInt(ldapPort))
                .root(ldapPartitionSuffix)
                .and()
                .passwordCompare()
                .passwordEncoder(new CustomBcryptPasswordEncoder())
                .passwordAttribute(ldapPasswordAttribute);

            // fallback for in-memory auth (ldap disabled)
        } else {
            auth.inMemoryAuthentication()
                .withUser(usernameInMemory)
                .password("{noop}" + userPasswordInMemory)
                .roles("USER")
                .and()
                .withUser(adminInMemory)
                .password("{noop}" + adminPasswordInMemory)
                .roles("ADMIN");
        }
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        /*
          Overloaded to expose Authenticationmanager's bean created by configure(AuthenticationManagerBuilder).
           This bean is used by the AuthenticationController.
         */
        return super.authenticationManagerBean();
    }

    private JwtTokenAuthenticationFilter jwtTokenAuthenticationFilter(String path, String secret) {
        return new JwtTokenAuthenticationFilter(path, secret);
    }

    // fallback for in-memory auth (ldap disabled)
    @Bean
    @Override
    public UserDetailsService userDetailsService() {

        //User Role
        UserDetails theUser = User.withUsername(usernameInMemory)
            .passwordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder()::encode)
            .password(userPasswordInMemory).roles("USER").build();

        //Manager Role
        UserDetails theManager = User.withUsername(adminInMemory)
            .passwordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder()::encode)
            .password(adminPasswordInMemory).roles("ADMIN").build();

        InMemoryUserDetailsManager userDetailsManager = new InMemoryUserDetailsManager();

        userDetailsManager.createUser(theUser);
        userDetailsManager.createUser(theManager);

        return userDetailsManager;
    }

    /**
     * CORS requests are managed only if headers Origin and Access-Control-Request-Method are available on OPTIONS requests
     * (this filter is simply ignored in other cases).
     * <p>
     * This filter can be used as a replacement for the @Cors annotation.
     **/
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

            // without this header jquery.ajax calls returns 401 even after successful login and SESSION-ID being successfully stored.
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
        public void init(FilterConfig arg0) {
        }
    }
}

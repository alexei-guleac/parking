package com.isd.parking.security.config;

import com.isd.parking.security.CustomPasswordEncoder;
import com.isd.parking.security.filter.JwtTokenAuthenticationFilter;
import com.isd.parking.security.filter.RestAccessDeniedHandler;
import com.isd.parking.security.filter.SecurityAuthenticationEntryPoint;
import com.isd.parking.utils.ColorConsoleOutput;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.ldap.core.ContextSource;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.ldap.SpringSecurityLdapTemplate;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@Slf4j
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final String[] pathArray = new String[]{"/auth/**", "/login", "/login/**", "/registration", "/validate_captcha", "/parking", "/arduino", "/demo"};

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
    private final LdapContextSource contextSource;
    @Value("${ldap.url}")
    private String ldapProviderUrl;

    @Autowired
    public WebSecurityConfiguration(LdapContextSource contextSource) {
        /*
         Ignores the default configuration, useless in our case (session management, etc..)
        */
        super(true);
        this.contextSource = contextSource;
    }

    @Bean
    LdapAuthoritiesPopulator ldapAuthoritiesPopulator() throws Exception {

        /*
          Specificity here : we don't get the Role by reading the members of available groups (which is implemented by
          default in Spring security LDAP), but we retrieve the groups from the field memberOf of the user.
         */
        class MyLdapAuthoritiesPopulator implements LdapAuthoritiesPopulator {

            private final String[] GROUP_ATTRIBUTE = {"cn"};
            private final String GROUP_MEMBER_OF = "memberOf";
            private SpringSecurityLdapTemplate ldapTemplate;

            MyLdapAuthoritiesPopulator(ContextSource contextSource) {
                ldapTemplate = new SpringSecurityLdapTemplate(contextSource);
            }

            @Override
            public Collection<? extends GrantedAuthority> getGrantedAuthorities(DirContextOperations userData, String username) {

                String roles = "";
                log.info("{getGrantedAuthorities} " + username + " userData " + userData);
                String[] groupDns = userData.getStringAttributes(GROUP_MEMBER_OF);
                log.info("{groupDns} " + Arrays.toString(groupDns));

                // if user entry contains memberOf attribute
                if (!(groupDns == null || groupDns.length == 0)) {
                    roles = Stream.of(groupDns).map(groupDn -> {
                        LdapName groupLdapName = (LdapName) ldapTemplate.retrieveEntry(groupDn, GROUP_ATTRIBUTE).getDn();
                        // split DN in its different components et get only the last one (cn = my_group)
                        // getValue() allows to only get get the value of the pair (cn => my_group)
                        return groupLdapName.getRdns().stream().map(Rdn::getValue).reduce((a, b) -> b).orElse(null);
                    }).map(x -> (String) x).collect(Collectors.joining(","));
                }
                return AuthorityUtils.commaSeparatedStringToAuthorityList(roles);
            }
        }

        return new MyLdapAuthoritiesPopulator(contextSource);
    }

    // ----------  LDAP auth --------------

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        /* the secret key used to sign the JWT token is known exclusively by the server.
         With Nimbus JOSE implementation, it must be at least 256 characters longs.
         */
        String secret = IOUtils.toString(Objects.requireNonNull(
                getClass().getClassLoader().getResourceAsStream("secret.key")), Charset.defaultCharset()
        );

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
        auth
                .ldapAuthentication()
                .userDnPatterns(ldapUserDnPattern)
                .userSearchBase(ldapSearchBase)
                .userSearchFilter(ldapUserSearchFilter)
                .groupSearchBase(ldapGroupSearchBase)
                .groupSearchFilter(ldapGroupSearchFilter)
                .ldapAuthoritiesPopulator(ldapAuthoritiesPopulator())
                .contextSource()
                .ldif(ldapFile)
                .url(ldapProviderUrl + "/" + ldapPartitionSuffix)
                .port(Integer.parseInt(ldapPort))
                .root(ldapPartitionSuffix)
                .and()
                .passwordCompare()
                .passwordEncoder(new CustomPasswordEncoder(new ColorConsoleOutput()))
                .passwordAttribute(ldapPasswordAttribute);
    }

    /*@Bean
    public BaseLdapPathContextSource contextSource() throws Exception {
        DefaultSpringSecurityContextSource contextSource = new DefaultSpringSecurityContextSource(ldapProviderUrl);
        // contextSource.setUserDn(providerUserDn);
        // contextSource.setPassword(providerPassword);
        return contextSource;
    }*/

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        /*
          Overloaded to expose Authenticationmanager's bean created by configure(AuthenticationManagerBuilder).
           This bean is used by the AuthenticationController.
         */
        return super.authenticationManagerBean();
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

    private JwtTokenAuthenticationFilter jwtTokenAuthenticationFilter(String path, String secret) {
        return new JwtTokenAuthenticationFilter(path, secret);
    }

    private CorsFilter corsFilter() {
        /*
         CORS requests are managed only if headers Origin and Access-Control-Request-Method are available on OPTIONS requests
         (this filter is simply ignored in other cases).

         This filter can be used as a replacement for the @Cors annotation.
        */
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader(ORIGIN);
        config.addAllowedHeader(CONTENT_TYPE);
        config.addAllowedHeader(ACCEPT);
        config.addAllowedHeader(AUTHORIZATION);
        config.addAllowedMethod(GET);
        config.addAllowedMethod(PUT);
        config.addAllowedMethod(POST);
        config.addAllowedMethod(OPTIONS);
        config.addAllowedMethod(DELETE);
        config.addAllowedMethod(PATCH);
        config.setMaxAge(3600L);

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
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

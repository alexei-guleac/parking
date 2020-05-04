package com.isd.parking.security.config.ldap;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.ldap.repository.config.EnableLdapRepositories;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;


/**
 * Configures application LDAP database connection
 */
@Configuration
@EnableLdapRepositories(basePackages = "com.isd.parking.** ** ")
@Slf4j
public class LdapClientConfiguration {

    private final Environment env;


    @Autowired
    public LdapClientConfiguration(Environment env) {
        this.env = env;
    }

    @Bean
    public @NotNull LdapContextSource contextSource() {

        @NotNull LdapContextSource contextSource = new LdapContextSource();
        contextSource.setUrl(env.getRequiredProperty("ldap.url"));
        contextSource.setBase(env.getRequiredProperty("ldap.partitionSuffix"));
        contextSource.afterPropertiesSet();

        return contextSource;
    }

    @Bean
    public @NotNull LdapTemplate ldapTemplate() {
        return new LdapTemplate(contextSource());
    }
}

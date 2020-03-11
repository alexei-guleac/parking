package com.isd.parking.security.config;

import com.isd.parking.utils.ColorConsoleOutput;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.schema.Schema;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.ldap.repository.config.EnableLdapRepositories;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

import java.util.Arrays;

import static com.isd.parking.utils.ColorConsoleOutput.blTxt;

@Configuration
@EnableLdapRepositories(basePackages = "com.isd.parking.** ** ")
@Slf4j
public class LdapClientConfiguration {

    private final Environment env;

    private final ColorConsoleOutput console;

    @Autowired
    public LdapClientConfiguration(Environment env, ColorConsoleOutput console) {
        this.env = env;
        this.console = console;
    }

    @Bean
    public LdapContextSource contextSource() {
        LdapContextSource contextSource = new LdapContextSource();

        /*try {
            setSchema();
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        contextSource.setUrl(env.getRequiredProperty("ldap.url"));
        contextSource.setBase(env.getRequiredProperty("ldap.partitionSuffix"));
        /*contextSource.setUserDn(env.getRequiredProperty("ldap.username"));
        contextSource.setPassword(env.getRequiredProperty("ldap.password"));*/

        log.info(console.classMsg(getClass().getSimpleName(),"ldap configuration"));
        log.info(blTxt(contextSource.getBaseLdapPathAsString()));
        log.info(blTxt(String.valueOf(contextSource.getBaseLdapName())));
        log.info(blTxt(String.valueOf(contextSource.getAuthenticationSource())));
        log.info(blTxt(Arrays.toString(contextSource.getUrls())));

        getDefaultSchema();
        contextSource.afterPropertiesSet();
        return contextSource;
    }

    /*public void setSchema() throws Exception {
        // Create the configuration to use for the server.
        InMemoryDirectoryServerConfig config = new InMemoryDirectoryServerConfig(env.getRequiredProperty("ldap.partitionSuffix"));
        config.setListenerConfigs(InMemoryListenerConfig.createLDAPConfig("LDAP", 8389));
        *//*config.setSchema(
                Schema.getSchema(
                        FileUtils.readPropertiesFiles("custom-schema.ldif")
                ));*//*
        log.info(console.methodMsg(String.valueOf(Schema.getDefaultStandardSchema())));

        // Create the directory server instance, populate it with data from the
        // "test-data.ldif" file, and start listening for client connections.
        InMemoryDirectoryServer ds = new InMemoryDirectoryServer(config);
        LDAPConnection connection = ds.getConnection();
        log.info(connection + " CONNECTION");
        //ds.importFromLDIF(true, FileUtils.readPropertiesFiles("ldap-server.ldif"));
        ds.startListening();
    }*/

    public void getDefaultSchema() {
        try {
            log.info(console.methodMsg(String.valueOf(Schema.getDefaultStandardSchema())));
        } catch (LDAPException e) {
            e.printStackTrace();
        }
    }

    @Bean
    public LdapTemplate ldapTemplate() {
        return new LdapTemplate(contextSource());
    }

    /*@Bean
    public UserService ldapClient() {
        return new UserService();
    }*/
}

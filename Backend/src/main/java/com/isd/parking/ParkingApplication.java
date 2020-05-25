package com.isd.parking;

import com.isd.parking.config.ApplicationProperties;
import com.isd.parking.config.SpringProfilesConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;


@Slf4j
@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)
@ComponentScan(basePackages = { "com.isd.parking.*" })
@Profile("default")
@EnableJpaRepositories
@EnableConfigurationProperties({ ApplicationProperties.class })
@PropertySources({@PropertySource("classpath:application.properties"),
    // database configuration file
    @PropertySource("classpath:postgresql-config-${spring.db-properties.prefix}.properties"),
    @PropertySource("classpath:documentation.properties")})
public class ParkingApplication {

    private final Environment env;

    public ParkingApplication(Environment env) {
        this.env = env;
    }

    /**
     * Main method, used to run the application.
     *
     * @param args the command line arguments.
     */
    public static void main(String[] args) {
        @NotNull SpringApplication application = new SpringApplication(ParkingApplication.class);
        @NotNull Environment env = application.run(args).getEnvironment();
        logApplicationStartup(env);
    }

    /**
     * Logs application startup
     *
     * @param env - Spring environment
     */
    private static void logApplicationStartup(@NotNull Environment env) {
        @NotNull String protocol = "http";
        if (env.getProperty("server.ssl.key-store") != null) {
            protocol = "https";
        }
        @Nullable String serverPort = env.getProperty("server.port");
        @Nullable String contextPath = env.getProperty("server.servlet.context-path");
        if (StringUtils.isBlank(contextPath)) {
            contextPath = "/";
        }
        String hostAddress = "localhost";
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.warn("The host name could not be determined, using `localhost` as fallback");
        }
        log.info("\n----------------------------------------------------------\n\t" +
                "Application '{}' is running! Access URLs:\n\t" +
                "Local: \t\t{}://localhost:{}{}\n\t" +
                "External: \t{}://{}:{}{}\n\t" +
                "Profile(s): \t{}\n----------------------------------------------------------",
            env.getProperty("spring.application.name"),
            protocol,
            serverPort,
            contextPath,
            protocol,
            hostAddress,
            serverPort,
            contextPath,
            env.getActiveProfiles());

        @Nullable String configServerStatus = env.getProperty("configserver.status");
        if (configServerStatus == null) {
            configServerStatus = "Not found or not setup for this application";
        }
        log.info("\n----------------------------------------------------------\n\t" +
                "Config Server: \t{}\n----------------------------------------------------------",
            configServerStatus);
    }

    /**
     * Initializes ParkingApplication.
     * <p>
     * Spring profiles can be configured with a program argument
     * --spring.profiles.active=your-active-profile
     */
    @PostConstruct
    public void initApplication() {
        @NotNull Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
        if (activeProfiles.contains(SpringProfilesConstants.SPRING_PROFILE_DEVELOPMENT)
            && activeProfiles.contains(SpringProfilesConstants.SPRING_PROFILE_PRODUCTION)) {
            log.error("You have misconfigured your application! It should not run " +
                "with both the 'dev' and 'prod' profiles at the same time.");
        }
        if (activeProfiles.contains(SpringProfilesConstants.SPRING_PROFILE_DEVELOPMENT)
            && activeProfiles.contains(SpringProfilesConstants.SPRING_PROFILE_CLOUD)) {
            log.error("You have misconfigured your application! It should not " +
                "run with both the 'dev' and 'cloud' profiles at the same time.");
        }
    }
}

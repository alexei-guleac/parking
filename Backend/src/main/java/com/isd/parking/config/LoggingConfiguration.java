package com.isd.parking.config;

import ch.qos.logback.classic.LoggerContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.jhipster.config.JHipsterProperties;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

import static io.github.jhipster.config.logging.LoggingUtils.*;


/**
 * Configures the console and Logstash log appenders from the app properties
 */
@Configuration
@RefreshScope
public class LoggingConfiguration {

    public LoggingConfiguration(@Value("${spring.application.name}") String appName,
                                @Value("${server.port}") String serverPort,
                                @NotNull JHipsterProperties jHipsterProperties,
                                @NotNull ObjectProvider<BuildProperties> buildProperties,
                                @NotNull ObjectMapper mapper) throws JsonProcessingException {

        @NotNull LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

        @NotNull Map<String, String> map = new HashMap<>();
        map.put("app_name", appName);
        map.put("app_port", serverPort);
        buildProperties.ifAvailable(it -> map.put("version", it.getVersion()));
        String customFields = mapper.writeValueAsString(map);

        JHipsterProperties.Logging loggingProperties = jHipsterProperties.getLogging();
        JHipsterProperties.Logging.Logstash logstashProperties = loggingProperties.getLogstash();

        if (loggingProperties.isUseJsonFormat()) {
            addJsonConsoleAppender(context, customFields);
        }
        if (logstashProperties.isEnabled()) {
            addLogstashTcpSocketAppender(context, customFields, logstashProperties);
        }
        if (loggingProperties.isUseJsonFormat() || logstashProperties.isEnabled()) {
            addContextListener(context, customFields, loggingProperties);
        }
        if (jHipsterProperties.getMetrics().getLogs().isEnabled()) {
            setMetricsMarkerLogbackFilter(context, loggingProperties.isUseJsonFormat());
        }
    }
}

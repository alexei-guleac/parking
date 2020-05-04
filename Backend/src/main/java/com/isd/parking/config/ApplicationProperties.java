package com.isd.parking.config;

import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * Properties specific to Parking Application.
 * <p>
 * Properties are configured in the {@code application.properties} file.
 * Database properties are configured in {@code postgresql-config.properties} file.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {
}

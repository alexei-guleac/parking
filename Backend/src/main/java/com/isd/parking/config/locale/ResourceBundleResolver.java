package com.isd.parking.config.locale;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Description;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;


/**
 * Reading messages from properties files
 */
@Component
public class ResourceBundleResolver {

    @Value("${spring.messages.basename}")
    private String basename;

    @Value("${spring.messages.encoding}")
    private String encoding;

    @Value("${spring.messages.fallback-to-system-locale}")
    private boolean fallback;

    @Value("${spring.messages.use-code-as-default-message}")
    private boolean useCode;

    /**
     * Configures messages source to display values from property files
     *
     * @return configured ResourceBundleMessageSource
     */
    @Bean
    @Description("Spring Message Resolver")
    public @NotNull ResourceBundleMessageSource emailMessageSource() {
        final @NotNull ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames(basename);
        messageSource.setDefaultEncoding(encoding);
        messageSource.setFallbackToSystemLocale(fallback);
        messageSource.setUseCodeAsDefaultMessage(useCode);

        return messageSource;
    }
}

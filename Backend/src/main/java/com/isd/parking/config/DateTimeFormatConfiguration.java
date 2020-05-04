package com.isd.parking.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.web.reactive.config.WebFluxConfigurer;

import javax.validation.constraints.NotNull;


/**
 * Configure the converters to use the ISO format for dates by default.
 * (for WebMvc)
 */
@Configuration
public class DateTimeFormatConfiguration implements WebFluxConfigurer {

    @Override
    @NotNull
    public void addFormatters(FormatterRegistry registry) {
        @org.jetbrains.annotations.NotNull DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
        registrar.setUseIsoFormat(true);
        registrar.registerFormatters(registry);
    }
}

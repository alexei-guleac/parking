package com.isd.parking.config.web;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.format.Formatter;
import org.springframework.format.FormatterRegistry;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.FileTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import javax.annotation.Nonnull;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;


/**
 * Spring MVC configuration
 * In this application used for email HTML templates
 */
@Configuration
@EnableWebMvc
@ComponentScan("com.isd.parking")
public class WebTemplateConfig implements ApplicationContextAware, WebMvcConfigurer {

    private static final String EMAIL_TEMPLATE_ENCODING = "UTF8";

    /**
     * Configures Thymeleaf Template Engine with specified view resolvers for different data types
     *
     * @return necessary engine
     */
    @Bean
    @Description("Thymeleaf Template Engine")
    public @NotNull TemplateEngine templateEngine() {
        @NotNull SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        // Resolver for HTML editable emails (which will be treated as a String)
        templateEngine.addTemplateResolver(htmlTemplateResolver());
        // Resolver for TEXT emails
        templateEngine.addTemplateResolver(textTemplateResolver());
        // Resolver for HTML emails (except the editable one)
        templateEngine.addTemplateResolver(stringTemplateResolver());
        templateEngine.setTemplateResolver(fileTemplateResolver());
        // Message source, internationalization specific to emails
        templateEngine.setTemplateEngineMessageSource(emailMessageSource());

        return templateEngine;
    }

    /**
     * Configures text template resolver
     *
     * @return text template resolver
     */
    private @NotNull ITemplateResolver textTemplateResolver() {
        @NotNull ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setOrder(1);
        templateResolver.setResolvablePatterns(Collections.singleton("text/*"));
        templateResolver.setPrefix("classpath:/templates/");
        templateResolver.setSuffix(".txt");
        templateResolver.setTemplateMode(TemplateMode.TEXT);
        templateResolver.setCharacterEncoding(EMAIL_TEMPLATE_ENCODING);
        templateResolver.setCheckExistence(true);
        templateResolver.setCacheable(false);

        return templateResolver;
    }

    /**
     * Configures .html template resolver
     *
     * @return .html template resolver
     */
    private @NotNull ITemplateResolver htmlTemplateResolver() {
        @NotNull ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setOrder(2);
        templateResolver.setResolvablePatterns(Collections.singleton("html/*"));
        templateResolver.setPrefix("classpath:/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCharacterEncoding(EMAIL_TEMPLATE_ENCODING);
        templateResolver.setCheckExistence(true);
        templateResolver.setCacheable(false);

        return templateResolver;
    }

    /**
     * Configures String template resolver
     *
     * @return String template resolver
     */
    private @NotNull ITemplateResolver stringTemplateResolver() {
        final @NotNull StringTemplateResolver templateResolver = new StringTemplateResolver();
        templateResolver.setOrder(3);
        // No resolvable pattern, will simply process as a String template everything not previously matched
        templateResolver.setTemplateMode("HTML5");
        templateResolver.setCacheable(false);

        return templateResolver;
    }

    /**
     * Configures file template resolver
     *
     * @return String template resolver
     */
    private @NotNull ITemplateResolver fileTemplateResolver() {
        @NotNull FileTemplateResolver templateResolver = new FileTemplateResolver();
        templateResolver.setOrder(4);
        templateResolver.setResolvablePatterns(Collections.singleton("files/*"));
        templateResolver.setPrefix("classpath:/templates/");            //Change based on your environment
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCharacterEncoding(EMAIL_TEMPLATE_ENCODING);
        templateResolver.setCheckExistence(true);
        templateResolver.setCacheable(false);

        return templateResolver;
    }

    /* *******************************************************************
        GENERAL CONFIGURATION ARTIFACTS
        Static Resources, i18n Messages, Formatters (Conversion Service)
       ******************************************************************* */

    /**
     * Add resource handlers for static files classpath directories for serves it
     *
     * @param registry - standard built-in Spring ResourceHandlerRegistry
     */
    @Override
    public void addResourceHandlers(@NonNull final ResourceHandlerRegistry registry) {
        WebMvcConfigurer.super.addResourceHandlers(registry);
        registry.addResourceHandler("/images/**").addResourceLocations("/images/");
        registry.addResourceHandler("/css/**").addResourceLocations("/css/");
        registry.addResourceHandler("/js/**").addResourceLocations("/js/");
    }

    /**
     * Configures messages source to display values from property files
     *
     * @return configured ResourceBundleMessageSource
     */
    @Bean
    @Description("Spring Message Resolver")
    public @NotNull ResourceBundleMessageSource emailMessageSource() {
        final @NotNull ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("mail/MailMessages");

        return messageSource;
    }

    /**
     * Adds necessary formatters for messages from message source
     *
     * @param registry configured
     */
    @Override
    public void addFormatters(@NonNull final FormatterRegistry registry) {
        WebMvcConfigurer.super.addFormatters(registry);
        registry.addFormatter(dateFormatter());
    }

    @Bean
    public @NotNull DateFormatter dateFormatter() {
        return new DateFormatter();
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
    }


    public static class DateFormatter implements Formatter<Date> {

        @Autowired
        private MessageSource messageSource;

        public DateFormatter() {
            super();
        }

        @Override
        @Nonnull
        public Date parse(@Nonnull final String text,
                          @Nonnull final Locale locale) throws ParseException {
            final @NotNull SimpleDateFormat dateFormat = createDateFormat(locale);
            return dateFormat.parse(text);
        }

        @Override
        @Nonnull
        public String print(@Nonnull final Date object,
                            @Nonnull final Locale locale) {
            final @NotNull SimpleDateFormat dateFormat = createDateFormat(locale);
            return dateFormat.format(object);
        }

        /**
         * Creates a date format depending on the specified locale
         *
         * @param locale - necessary locale
         * @return formatted SimpleDateFormat
         */
        private @NotNull SimpleDateFormat createDateFormat(final @NotNull Locale locale) {
            final @NotNull String format = this.messageSource.getMessage("date.format", null, locale);
            final @NotNull SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            dateFormat.setLenient(false);

            return dateFormat;
        }
    }
}

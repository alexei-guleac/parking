package com.isd.parking.config.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.web.ReactivePageableHandlerMethodArgumentResolver;
import org.springframework.data.web.ReactiveSortHandlerMethodArgumentResolver;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.result.method.annotation.ArgumentResolverConfigurer;
import org.springframework.web.server.WebExceptionHandler;
import org.zalando.problem.ProblemModule;
import org.zalando.problem.spring.webflux.advice.ProblemExceptionHandler;
import org.zalando.problem.spring.webflux.advice.ProblemHandling;
import org.zalando.problem.spring.webflux.advice.security.SecurityAdviceTrait;
import org.zalando.problem.violations.ConstraintViolationProblemModule;


/**
 * Configuration of web application with Servlet 3.0 APIs.
 * Configured for Problem module Spring Web Flux future support
 */
@Configuration
public class WebProblemModuleConfigurer implements WebFluxConfigurer {

    // TODO: remove when this is supported in spring-data / spring-boot
    @Override
    public void configureArgumentResolvers(@NotNull ArgumentResolverConfigurer configurer) {
        configurer.addCustomResolver(new ReactiveSortHandlerMethodArgumentResolver(),
            new ReactivePageableHandlerMethodArgumentResolver());
    }

    @Bean
    public @NotNull ProblemModule problemModule() {
        return new ProblemModule();
    }

    @Bean
    public @NotNull ConstraintViolationProblemModule constraintViolationProblemModule() {
        return new ConstraintViolationProblemModule();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper().registerModules(
            new ProblemModule(),
            new ConstraintViolationProblemModule());
    }

    @Bean
    @Order(-2)
    // The handler must have precedence
    // over WebFluxResponseStatusExceptionHandler and Spring Boot's ErrorWebExceptionHandler
    public @NotNull WebExceptionHandler problemExceptionHandler(@NotNull ObjectMapper mapper,
                                                                @NotNull ProblemHandling problemHandling) {
        return new ProblemExceptionHandler(mapper, problemHandling);
    }

    @ControllerAdvice
    public class ExceptionHandler implements ProblemHandling {
    }

    @ControllerAdvice
    public class SecurityExceptionHandler implements SecurityAdviceTrait {
    }
}

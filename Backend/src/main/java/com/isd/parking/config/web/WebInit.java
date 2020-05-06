package com.isd.parking.config.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.Filter;


@Configuration
public class WebInit extends AbstractAnnotationConfigDispatcherServletInitializer {
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[]{};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[]{WebTemplateConfig.class};
    }

    @Override
    @NonNull
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    @Override
    protected Filter[] getServletFilters() {
          /*
        If the JwtTokenAuthenticationFilter was directly used as a ServletFilter, then only this filter would be applied.
        In this case, chained filters managed by Spring Security (ExceptionTranslationFilter, SessionManagementFilter et FilterSecurityInterceptor, etc.)
        wouldn't be applied. As such, URL filtering wouldn't be secured as expected by the configuration).

        We need to specify the springSecurityFilterChain as the initial Servlet filter. This proxy takes care of chaining filter calls as they
         are indicated in the WebSecurityConfiguration class.
        **/
        return new Filter[]{new DelegatingFilterProxy("springSecurityFilterChain")};
    }
}

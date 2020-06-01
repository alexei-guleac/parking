package com.isd.parking.config.locale;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.isd.parking.config.locale.Localization.localeList;
import static org.springframework.http.HttpHeaders.ACCEPT_LANGUAGE;


/**
 * Class for resolving locale from HTTP "Accept-Language" header
 */
@Component
@Slf4j
public class SmartLocaleResolver extends AcceptHeaderLocaleResolver {

    /**
     * Extracts locale from HTTP request headers
     *
     * @param request - target request
     * @return result Locale
     */
    @Override
    public @NotNull Locale resolveLocale(HttpServletRequest request) {
        final String localeHeader = request.getHeader(ACCEPT_LANGUAGE);
        return getLocaleFromHeader(localeHeader);
    }

    /**
     * Extracts locale directly from HTTP headers
     *
     * @param headers - target headers
     * @return result Locale
     */
    public @NotNull Locale resolveLocale(Map<String, String> headers) {
        final String localeHeader = headers.get(ACCEPT_LANGUAGE.toLowerCase());

        return getLocaleFromHeader(localeHeader);
    }

    @NotNull
    private Locale getLocaleFromHeader(String localeHeader) {
        List<Locale.LanguageRange> ranges;
        Locale locale = Locale.getDefault();

        if (localeHeader != null && !StringUtils.isBlank(localeHeader)) {
            ranges = Locale.LanguageRange.parse(localeHeader);
            // check wrong syntax
            if (!ranges.isEmpty() && !localeList.isEmpty()) {
                final Locale lookup = Locale.lookup(ranges, localeList);

                return lookup;
            }
        }
        return locale;
    }

    @Bean
    public SmartLocaleResolver localeResolver() {
        final SmartLocaleResolver resolver = new SmartLocaleResolver();
        resolver.setSupportedLocales(localeList);
        resolver.setDefaultLocale(Locale.ENGLISH);

        return resolver;
    }
}



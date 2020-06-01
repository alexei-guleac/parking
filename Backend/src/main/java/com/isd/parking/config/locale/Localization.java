package com.isd.parking.config.locale;

import java.util.*;


/**
 * Class of available server locales
 */
public class Localization {

    /**
     * Available server language shorthands
     */
    public static final String[] languages = {"ru", "ro", "en"};

    /**
     * Storage of available server locales
     */
    public static final Map<String, Locale> LOCALE_MAP = new HashMap<>() {{
        put(languages[0], new Locale(languages[0], languages[0].toUpperCase()));
        put(languages[1], new Locale(languages[1], languages[1].toUpperCase()));
        put(languages[2], new Locale(languages[2], "US"));
    }};

    public static final String defaultLanguage = languages[2];

    final static List<Locale> localeList = new ArrayList<>(LOCALE_MAP.values());

    /**
     * Get locale from language shorthand or return default server locale
     *
     * @param language - target requested language
     * @return specified Locale object
     */
    public static Locale getLocale(String language) {
        return LOCALE_MAP.getOrDefault(language, Locale.getDefault());
    }
}

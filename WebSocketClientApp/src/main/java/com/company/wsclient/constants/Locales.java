package com.company.wsclient.constants;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class Locales {

    public static final String[] languages = {"ru", "ro", "en"};

    public static final Map<String, Locale> LOCALE_MAP = new HashMap<>() {{
        put(languages[0], new Locale(languages[0], languages[0].toUpperCase()));
        put(languages[1], new Locale(languages[1], languages[1].toUpperCase()));
        put(languages[2], new Locale(languages[2], "US"));
    }};
}

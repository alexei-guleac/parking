package com.company.wsclient.constants;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class Locales {
    public static final Map<String, Locale> languages = new HashMap<>() {{
        put("ru", new Locale("ru", "RU"));
        put("ro", new Locale("ro", "RO"));
        put("en", new Locale("en", "EN"));
    }};
}

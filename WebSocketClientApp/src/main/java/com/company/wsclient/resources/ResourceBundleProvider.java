package com.company.wsclient.resources;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;


public class ResourceBundleProvider {

    private static ResourceBundleProvider INSTANCE;

    private static ResourceBundle messages = ResourceBundle.getBundle("strings", Locale.getDefault());

    public static String getString(String key) {
        return messages.getString(key);
    }

    public static String getString(String key, Object... param) {
        return MessageFormat.format(messages.getString(key), param);
    }

    public static ResourceBundleProvider getInstance() {
        if (INSTANCE == null) {
            synchronized (ResourceBundleProvider.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ResourceBundleProvider();
                }
            }
        }
        return INSTANCE;
    }

    public static void setLocale(Locale locale) {
        messages = ResourceBundle.getBundle("strings", locale);
    }
}

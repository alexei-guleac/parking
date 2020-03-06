package com.isd.parking.utils;

import org.apache.logging.log4j.util.PropertiesUtil;

import java.util.Objects;

public class FileUtils {
    public static String readPropertiesFiles(String filename) {
        ClassLoader classLoader = PropertiesUtil.class.getClassLoader();
        String path = Objects.requireNonNull(classLoader.getResource(filename)).getPath();
        System.out.println(path);

        return path;
    }
}

package com.isd.parking.utils;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Objects;

public class FileUtils {
    public String getResourceAsString(String resourceName) {

        String str = "";
        try {
            str = IOUtils.toString(
                    Objects.requireNonNull(
                            getClass().getClassLoader().getResourceAsStream(resourceName)),
                    Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return str;
    }
}

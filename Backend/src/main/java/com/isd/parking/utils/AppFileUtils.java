package com.isd.parking.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;


/**
 * File utilities
 */
@Slf4j
public class AppFileUtils {

    /**
     * Read Spring application classpath resource file as String
     * Used for single request (do not use for files that are updating in the application during working process)
     * (!may cause problems returning same file stream (not reloading)
     * !not useful for in-memory file reload before stream opening)
     * https://github.com/microsoft/vscode-java-debug/issues/226
     * https://stackoverflow.com/questions/3121449/getclass-getclassloader-getresourceasstream-is-caching-the-resource
     *
     * @param resourceName - classpath resource file name
     * @return String representation of classpath resource file
     */
    public String getResourceAsString(String resourceName) {
        String str = "";
        try {
            str = IOUtils.toString(
                Objects.requireNonNull(getFileInputStream(resourceName)), Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * Get file content as String in Standard UTF-8 charset
     *
     * @param path - absolute path to file
     * @return file content as String
     */
    public String getFileAsString(String path) {
        String str = "";
        try {
            str = Files.readString(Paths.get(path), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return str;
    }

    /**
     * Get Spring application classpath resource file input stream
     *
     * @param resourceName - Spring application classpath resource file
     * @return InputStream from specified classpath resource file
     * @throws IOException
     */
    private InputStream getFileInputStream(String resourceName) throws IOException {
        Resource resource = new ClassPathResource(resourceName);
        return resource.getInputStream();
    }
}

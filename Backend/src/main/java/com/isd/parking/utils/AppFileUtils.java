package com.isd.parking.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

import static com.isd.parking.utils.ColorConsoleOutput.methodMsgStatic;


@Slf4j
public class AppFileUtils {

    // may cause problems returning same file stream (not reloading)
    // https://github.com/microsoft/vscode-java-debug/issues/226
    // https://stackoverflow.com/questions/3121449/getclass-getclassloader-getresourceasstream-is-caching-the-resource
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

    public String getFileAsString(String path) {
        String str = "";
        log.info(methodMsgStatic(" path" + path));
        try {
            str = Files.readString(Paths.get(path), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info(methodMsgStatic(" file content entries" + str));
        return str;
    }

    private InputStream getFileInputStream(String resourceName) throws IOException {
        // not working
        // return Objects.requireNonNull(getClass().getClassLoader().getResource(resourceName)).openStream();

        // return new FileInputStream(ResourceUtils.getFile("classpath:" + resourceName));

        // not working
        Resource resource = new ClassPathResource(resourceName);
        InputStream input = resource.getInputStream();
        File file = resource.getFile();
        return input;

    }

    public static int binarySearch(String word, String[] words, int a, int b) {
        if (b <= a)
            return -1;
        if (b - a == 1)
            return words[a].equals(word) ? a : -1;

        int pivot = (a + b) / 2;
        if (word.compareTo(words[pivot]) < 0) {
            return binarySearch(word, words, 0, pivot);
        } else if (word.compareTo(words[pivot]) > 0) {
            return binarySearch(word, words, pivot, b);
        }

        return pivot;
    }

    public static int binarySearchLdifEntry(String word, String[] words, int a, int b) {
        if (b <= a)
            return -1;
        if (b - a == 1)
            return words[a].equals(word) ? a : -1;

        int pivot = (a + b) / 2;
        if (word.compareTo(getEntryFirstLine(words[pivot])) < 0) {
            return binarySearch(word, words, 0, pivot);
        } else if (word.compareTo(getEntryFirstLine(words[pivot])) > 0) {
            return binarySearch(word, words, pivot, b);
        }

        return pivot;
    }

    public static String getEntryFirstLine(String entry) {
        String commonLineSplit = "\n";
        String alternativeLineSplit = "\r\n";
        return entry.split(commonLineSplit)[0];
    }
}

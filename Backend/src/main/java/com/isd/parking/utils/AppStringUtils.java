package com.isd.parking.utils;

import com.ibm.icu.text.Transliterator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.isd.parking.utils.ColorConsoleOutput.methodMsgStatic;
import static org.apache.commons.lang.StringUtils.strip;


@Slf4j
public class AppStringUtils {

    public String collectionToString(Collection e) {
        return strip(strip(Arrays.toString(e.toArray()), "["), "]");
    }

    public static String convertArrayToString(String[] strArray, String separator) {
        return String.join(separator, strArray);
    }

    public static String convertArrayToCommaSeparatedString(String[] strArray) {
        return String.join(",", strArray);
    }

    public static String generateCommonLangPassword() {

        String upperCaseLetters = RandomStringUtils.random(1, 65, 90, true, true);
        String lowerCaseLetters = RandomStringUtils.random(2, 97, 122, true, true);
        String numbers = RandomStringUtils.randomNumeric(1);
        String specialChar = RandomStringUtils.random(1, 33, 47, false, false);
        String totalChars = RandomStringUtils.randomAlphanumeric(2);

        String combinedChars = upperCaseLetters.concat(lowerCaseLetters)
            .concat(numbers)
            .concat(specialChar)
            .concat(totalChars);

        List<Character> pwdChars = combinedChars.chars()
            .mapToObj(c -> (char) c)
            .collect(Collectors.toList());
        Collections.shuffle(pwdChars);

        return pwdChars.stream()
            .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
            .toString();
    }

    public static boolean isCyrillicString(String source) {
        final boolean matches = Pattern.matches("^.*\\p{InCyrillic}.*$", source);
        log.info(methodMsgStatic("matches: " + matches));
        return matches;
    }

    public static String transliterateCyrillicToLatin(String source) {
        var CYRILLIC_TO_LATIN = "Cyrillic-Latin; nfd; [:nonspacing mark:] remove; nfc;";
        Transliterator toLatinTrans = Transliterator.getInstance(CYRILLIC_TO_LATIN);
        final String transliterate = toLatinTrans.transliterate(source);
        log.info(methodMsgStatic("transliterate: " + transliterate));
        return transliterate;
    }
}

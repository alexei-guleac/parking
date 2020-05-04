package com.isd.parking.utilities;

import com.ibm.icu.text.Transliterator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.apache.commons.lang.StringUtils.strip;


/**
 * Application string processing utilities
 */
@Slf4j
public class AppStringUtils {

    /**
     * Converts String array to String separated by specified element separator
     *
     * @param strArray  - target array of Strings
     * @param separator - specified array elements separator
     * @return array separated String representation
     */
    public static @NotNull String convertArrayToString(String[] strArray, @NotNull String separator) {
        return String.join(separator, strArray);
    }

    /**
     * Converts String array to String separated by comma
     *
     * @param strArray - target array of Strings
     * @return array comma separated String representation
     */
    public static @NotNull String convertArrayToCommaSeparatedString(String[] strArray) {
        return String.join(",", strArray);
    }

    /**
     * Generates eight-character passwords, each with a minimum of
     * two lower case characters, one uppercase characters, two digits and one special character
     *
     * @return password String
     */
    public static @NotNull String generateCommonLangPassword() {

        @NotNull String upperCaseLetters = RandomStringUtils.random(1, 65, 90, true, true);
        @NotNull String lowerCaseLetters = RandomStringUtils.random(2, 97, 122, true, true);
        @NotNull String numbers = RandomStringUtils.randomNumeric(2);
        @NotNull String specialChar = RandomStringUtils.random(1, 33, 47, false, false);
        @NotNull String totalChars = RandomStringUtils.randomAlphanumeric(2);

        @NotNull String combinedChars = upperCaseLetters.concat(lowerCaseLetters)
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

    /**
     * Verifies that String for the most part is in cyrillic symbols
     *
     * @param source - target String
     * @return operation result
     */
    public static boolean isCyrillicString(String source) {
        return Pattern.matches("^.*\\p{InCyrillic}.*$", source);
    }

    /**
     * Transliterates given source String from cyrillic symbols to latin symbols using
     * com.ibm.icu.text.Transliterator library
     *
     * @param source - target String
     * @return transliterated String representation in latin symbols
     */
    public static String transliterateCyrillicToLatin(@NotNull String source) {
        @NotNull var CYRILLIC_TO_LATIN = "Cyrillic-Latin; nfd; [:nonspacing mark:] remove; nfc;";
        Transliterator toLatinTrans = Transliterator.getInstance(CYRILLIC_TO_LATIN);

        return toLatinTrans.transliterate(source);
    }

    /**
     * Converts collection to standard Java String representation without start and end square brackets
     *
     * @param collection - some target collection
     * @return collection String standard representation
     */
    public String collectionToString(@NotNull Collection<?> collection) {
        return strip(strip(Arrays.toString(collection.toArray()), "["), "]");
    }
}

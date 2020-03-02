package com.isd.parking.utils;

import org.springframework.stereotype.Component;

@Component
public class ColorConsoleOutput {

    // Reset
    public static final String RESET = "\033[0m";       // Text Reset

    // Regular Colors
    public static final String BLACK = "\033[0;30m";   // BLACK
    public static final String RED = "\033[0;31m";     // RED
    public static final String GREEN = "\033[0;32m";   // GREEN
    public static final String YELLOW = "\033[0;33m";  // YELLOW
    public static final String BLUE = "\033[0;34m";    // BLUE
    public static final String PURPLE = "\033[0;35m";  // PURPLE
    public static final String CYAN = "\033[0;36m";    // CYAN
    public static final String WHITE = "\033[0;37m";   // WHITE

    // Bold
    public static final String BLACK_BOLD = "\033[1;30m";  // BLACK
    public static final String RED_BOLD = "\033[1;31m";    // RED
    public static final String GREEN_BOLD = "\033[1;32m";  // GREEN
    public static final String YELLOW_BOLD = "\033[1;33m"; // YELLOW
    public static final String BLUE_BOLD = "\033[1;34m";   // BLUE
    public static final String PURPLE_BOLD = "\033[1;35m"; // PURPLE
    public static final String CYAN_BOLD = "\033[1;36m";   // CYAN
    public static final String WHITE_BOLD = "\033[1;37m";  // WHITE

    // Underline
    public static final String BLACK_UNDERLINED = "\033[4;30m";  // BLACK
    public static final String RED_UNDERLINED = "\033[4;31m";    // RED
    public static final String GREEN_UNDERLINED = "\033[4;32m";  // GREEN
    public static final String YELLOW_UNDERLINED = "\033[4;33m"; // YELLOW
    public static final String BLUE_UNDERLINED = "\033[4;34m";   // BLUE
    public static final String PURPLE_UNDERLINED = "\033[4;35m"; // PURPLE
    public static final String CYAN_UNDERLINED = "\033[4;36m";   // CYAN
    public static final String WHITE_UNDERLINED = "\033[4;37m";  // WHITE

    // Background
    public static final String BLACK_BACKGROUND = "\033[40m";  // BLACK
    public static final String RED_BACKGROUND = "\033[41m";    // RED
    public static final String GREEN_BACKGROUND = "\033[42m";  // GREEN
    public static final String YELLOW_BACKGROUND = "\033[43m"; // YELLOW
    public static final String BLUE_BACKGROUND = "\033[44m";   // BLUE
    public static final String PURPLE_BACKGROUND = "\033[45m"; // PURPLE
    public static final String CYAN_BACKGROUND = "\033[46m";   // CYAN
    public static final String WHITE_BACKGROUND = "\033[47m";  // WHITE

    // High Intensity
    public static final String BLACK_BRIGHT = "\033[0;90m";  // BLACK
    public static final String RED_BRIGHT = "\033[0;91m";    // RED
    public static final String GREEN_BRIGHT = "\033[0;92m";  // GREEN
    public static final String YELLOW_BRIGHT = "\033[0;93m"; // YELLOW
    public static final String BLUE_BRIGHT = "\033[0;94m";   // BLUE
    public static final String PURPLE_BRIGHT = "\033[0;95m"; // PURPLE
    public static final String CYAN_BRIGHT = "\033[0;96m";   // CYAN
    public static final String WHITE_BRIGHT = "\033[0;97m";  // WHITE

    // Bold High Intensity
    public static final String BLACK_BOLD_BRIGHT = "\033[1;90m"; // BLACK
    public static final String RED_BOLD_BRIGHT = "\033[1;91m";   // RED
    public static final String GREEN_BOLD_BRIGHT = "\033[1;92m"; // GREEN
    public static final String YELLOW_BOLD_BRIGHT = "\033[1;93m";// YELLOW
    public static final String BLUE_BOLD_BRIGHT = "\033[1;94m";  // BLUE
    public static final String PURPLE_BOLD_BRIGHT = "\033[1;95m";// PURPLE
    public static final String CYAN_BOLD_BRIGHT = "\033[1;96m";  // CYAN
    public static final String WHITE_BOLD_BRIGHT = "\033[1;97m"; // WHITE

    // High Intensity backgrounds
    public static final String BLACK_BACKGROUND_BRIGHT = "\033[0;100m"; // BLACK
    public static final String RED_BACKGROUND_BRIGHT = "\033[0;101m";   // RED
    public static final String GREEN_BACKGROUND_BRIGHT = "\033[0;102m"; // GREEN
    public static final String YELLOW_BACKGROUND_BRIGHT = "\033[0;103m";// YELLOW
    public static final String BLUE_BACKGROUND_BRIGHT = "\033[0;104m";  // BLUE
    public static final String PURPLE_BACKGROUND_BRIGHT = "\033[0;105m";// PURPLE
    public static final String CYAN_BACKGROUND_BRIGHT = "\033[0;106m";  // CYAN

    public static String ywTxt(String str) {
        return drawColorString(str, YELLOW);
    }

    public static String ywBrTxt(String str) {
        return drawColorString(str, YELLOW_BRIGHT);
    }

    public static String grTxt(String str) {
        return drawColorString(str, GREEN);
    }

    public static String grBrTxt(String str) {
        return drawColorString(str, GREEN_BRIGHT);
    }

    public static String blTxt(String str) {
        return drawColorString(str, BLUE);
    }

    public static String cyBrTxt(String str) {
        return drawColorString(str, CYAN_BRIGHT);
    }

    public static String puBrTxt(String str) {
        return drawColorString(str, PURPLE_BRIGHT);
    }

    public static String redTxt(String str) {
        return drawColorString(str, RED);
    }

    public static String redBrTxt(String str) {
        return drawColorString(str, RED_BRIGHT);
    }

    public static String whTxt(String str) {
        return drawColorString(str, WHITE);
    }

    private static String drawColorString(String str, String textColor) {
        return textColor + str + RESET;
    }

    private static String drawColorStringBg(String str, String textColor, String bgColor) {
        return textColor + bgColor + str + RESET;
    }

    public String classMsg(String msg) {
        return cyBrTxt("{" + getClass().getSimpleName() + "} ") + grTxt(msg);
    }

    public String methodMsg(String msg) {
        return grTxt(msg) + " executing in " + puBrTxt("{" + ReflectionMethods.getMethodName(0) + "} ");
    }
}

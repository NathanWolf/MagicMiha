package com.elmakers.mine.bukkit.miha.utilities;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class ChatUtils {
    private static final Pattern PUNCTUATION_PATTERN = Pattern.compile("[\\s0-9\\p{Punct}\\p{IsPunctuation}\u2150-\u2BFF\uE000-\uFFFF]"); // Sorry for the unicode escapes

    public static String[] getWords(String text) {
        return PUNCTUATION_PATTERN.split(text);
    }

    public static String getSimpleMessage(String text) {
        // Not copying over all the json logic
        return text;
    }

    public static String getFixedWidth(String message, int width) {
        int messageLength = getSimpleMessage(message).length();
        if (messageLength < width) {
            message = message + StringUtils.repeat(" ", width - messageLength);
        } else if (messageLength > width) {
            message = getSimpleMessage(message).substring(0, width);
        }
        return message;
    }

    public static String printPercentage(double percentage) {
        return printRatio(percentage) + "%";
    }

    public static String printRatio(double percentage) {
    return Integer.toString((int)(percentage * 100));
}
}

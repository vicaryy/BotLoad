package org.vicary.pattern;

import java.util.Arrays;

public class InstagramPattern {
    public static boolean checkURLValidation(String instagramURL) {
        return instagramURL.contains("instagram.com/");
    }

    public static String getURL(String text) {
        return Arrays.stream(text.split(" ")).findFirst().orElse("");
    }
}

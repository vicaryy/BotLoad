package org.vicary.pattern;

import java.util.Arrays;

public class TikTokPattern {
    public static boolean checkURLValidation(String tiktokURL) {
        return tiktokURL.contains("tiktok.com/");
    }

    public static String getURL(String text) {
        return Arrays.stream(text.split(" ")).findFirst().orElse("");
    }
}

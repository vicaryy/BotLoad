package org.vicary.pattern;

import java.util.Arrays;

public class TwitterPattern {

    public static boolean checkURLValidation(String twitterUrl) {
        return twitterUrl.contains("twitter.com/");
    }

    public static String getUrl(String text) {
        return Arrays.stream(text.split(" ")).findFirst().orElse("");
    }
}

package org.vicary.pattern.twitter;

import java.util.Arrays;

public class TwitterPattern {

    public static boolean checkUrlValidation(String twitterUrl) {
        return twitterUrl.contains("twitter.com/");
    }

    public static String getUrl(String text) {
        return Arrays.stream(text.split(" ")).findFirst().orElse("");
    }
}

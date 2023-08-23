package org.vicary.pattern;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YoutubePattern {
    public static String getYoutubeId(String youtubeURL) {
        Pattern pattern = Pattern.compile(
                "http(?:s)?:\\/\\/(?:m.)?(?:www\\.)?youtu(?:\\.be\\/|(?:be-nocookie|be)\\.com\\/(?:watch|[\\w]+\\?(?:feature=[\\w]+.[\\w]+\\&)?v=|v\\/|e\\/|embed\\/|live\\/|shorts\\/|user\\/(?:[\\w#]+\\/)+))([^&#?\\n]+)",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(youtubeURL);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    public static boolean checkURLValidation(String youtubeURL) {
        Pattern pattern = Pattern.compile(
                "http(?:s)?:\\/\\/(?:m.)?(?:www\\.)?youtu(?:\\.be\\/|(?:be-nocookie|be)\\.com\\/(?:watch|[\\w]+\\?(?:feature=[\\w]+.[\\w]+\\&)?v=|v\\/|e\\/|embed\\/|live\\/|shorts\\/|user\\/(?:[\\w#]+\\/)+))([^&#?\\n]+)",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(youtubeURL);
        if (matcher.find()) {
            return matcher.group(1).length() == 11;
        }
        return false;
    }

    public static String getUrl(String text) {
        return Arrays.stream(text.split(" ")).findFirst().orElse("");
    }
}

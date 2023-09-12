package org.vicary.pattern;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;

@Component
public class Pattern {
    public String getYoutubeId(String youtubeURL) {
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(
                "http(?:s)?:\\/\\/(?:m.)?(?:www\\.)?youtu(?:\\.be\\/|(?:be-nocookie|be)\\.com\\/(?:watch|[\\w]+\\?(?:feature=[\\w]+.[\\w]+\\&)?v=|v\\/|e\\/|embed\\/|live\\/|shorts\\/|user\\/(?:[\\w#]+\\/)+))([^&#?\\n]+)",
                java.util.regex.Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(youtubeURL);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }

    public boolean isYouTubeURL(String youtubeURL) {
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(
                "http(?:s)?:\\/\\/(?:m.)?(?:www\\.)?youtu(?:\\.be\\/|(?:be-nocookie|be)\\.com\\/(?:watch|[\\w]+\\?(?:feature=[\\w]+.[\\w]+\\&)?v=|v\\/|e\\/|embed\\/|live\\/|shorts\\/|user\\/(?:[\\w#]+\\/)+))([^&#?\\n]+)",
                java.util.regex.Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(youtubeURL);
        if (matcher.find()) {
            return matcher.group(1).length() == 11;
        }
        return false;
    }

    public boolean isTwitterURL(String URL) {
        return URL.contains("twitter.com/") || URL.contains("x.com/");
    }

    public boolean isTikTokURL(String URL) {
        return URL.contains("tiktok.com/");
    }

    public boolean isInstagramURL(String URL) {
        return URL.contains("instagram.com/");
    }

    public boolean isSoundCloudURL(String URL) {
        return URL.contains("soundcloud.com/");
    }
}

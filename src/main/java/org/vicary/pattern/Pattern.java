package org.vicary.pattern;

import org.springframework.stereotype.Component;

import java.util.Arrays;
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
        return null;
    }

    public boolean isYouTubeURLValid(String youtubeURL) {
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(
                "http(?:s)?:\\/\\/(?:m.)?(?:www\\.)?youtu(?:\\.be\\/|(?:be-nocookie|be)\\.com\\/(?:watch|[\\w]+\\?(?:feature=[\\w]+.[\\w]+\\&)?v=|v\\/|e\\/|embed\\/|live\\/|shorts\\/|user\\/(?:[\\w#]+\\/)+))([^&#?\\n]+)",
                java.util.regex.Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(youtubeURL);
        if (matcher.find()) {
            return matcher.group(1).length() == 11;
        }
        return false;
    }

    public boolean isTwitterURLValid(String twitterURL) {
        return twitterURL.contains("twitter.com/");
    }

    public boolean isTikTokURLValid(String tiktokURL) {
        return tiktokURL.contains("tiktok.com/");
    }

    public boolean isInstagramURLValid(String instagramURL) {
        return instagramURL.contains("instagram.com/");
    }
}

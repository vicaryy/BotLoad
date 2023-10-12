package org.vicary.pattern;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runner.Runner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PatternTest {
    @Autowired
    private Pattern pattern;

    @Test
    void getYouTubeId_expectEquals_ValidURL() {
        String expectedId = "EXAMPLE__ID";
        List<String> validURLs = Arrays.asList(
                "https://www.youtube.com/v/EXAMPLE__ID",
                "http://www.youtube.com/v/EXAMPLE__ID",
                "https://m.youtube.com/watch?v=EXAMPLE__ID",
                "https://www.youtube.com/watch?v=EXAMPLE__ID&list=RDMM&start_radio=1",
                "https://m.youtube.com/v/EXAMPLE__ID",
                "http://www.youtube.com/user/SomeUser#p/a/u/1/EXAMPLE__ID",
                "http://youtu.be/EXAMPLE__ID",
                "http://www.youtube.com/embed/EXAMPLE__ID?rel=0",
                "https://youtube.com/shorts/EXAMPLE__ID?feature=share",
                "https://youtube.com/live/EXAMPLE__ID"
                );

        for (String URL : validURLs) {
            assertEquals(expectedId, pattern.getYoutubeId(URL));
        }
    }

    @Test
    void getYouTubeId_expectNotEquals_InvalidURL() {
        String expectedId = "EXAMPLE__ID";

        List<String> invalidURLs = Arrays.asList(
                "http://www.youtube.com/watch?dev=inprogress&v=EXAMPLE__ID&feature=related",
                "youtube.com/v/EXAMPLE__ID",
                "youtube.com/watch?v=EXAMPLE__ID",
                "youtu.be/EXAMPLE__ID",
                "www.youtube.com/v/EXAMPLE__ID",
                "youtube.com/watch?v=EXAMPLE__ID&wtv=wtv",
                "http://www.youtube.com/watch?dev=inprogress&v=EXAMPLE__ID&feature=related"
        );

        for (String URL : invalidURLs) {
            assertNotEquals(expectedId, pattern.getYoutubeId(URL));
        }
    }

    @Test
    void isYouTubeURL_expectFalse_InvalidURL() {
        String URL = "youtube.com/v/EXAMPLE__ID";

        assertFalse(pattern.isYouTubeURL(URL));
    }

    @Test
    void isYouTubeURL_expectTrue_ValidURL() {
        String URL = "https://www.youtube.com/v/EXAMPLE__ID";

        assertTrue(pattern.isYouTubeURL(URL));
    }

    @Test
    void isYouTubeURL_expectFalse_YouTubeIdAbove11Letters() {
        String URL = "https://www.youtube.com/v/EXAMPLE__IDD";

        assertFalse(pattern.isYouTubeURL(URL));
    }

    @Test
    void isTwitterURL_expectTrue_ValidURL() {
        String URL = "JUST CONTAINS twitter.com/...";

        assertTrue(pattern.isTwitterURL(URL));
    }

    @Test
    void isTwitterURL_expectFalse_InvalidURL() {
        String URL = "twitter.pl/";

        assertFalse(pattern.isTwitterURL(URL));
    }

    @Test
    void isTikTokURL_expectTrue_ValidURL() {
        String URL = "JUST CONTAINS tiktok.com/...";

        assertTrue(pattern.isTikTokURL(URL));
    }

    @Test
    void isTikTokURL_expectFalse_InvalidURL() {
        String URL = "tiktok.pl/";

        assertFalse(pattern.isTikTokURL(URL));
    }

    @Test
    void isInstagramURL_expectTrue_ValidURL() {
        String URL = "JUST CONTAINS instagram.com/...";

        assertTrue(pattern.isInstagramURL(URL));
    }

    @Test
    void isInstagramURL_expectFalse_InvalidURL() {
        String URL = "instagram.pl/";

        assertFalse(pattern.isInstagramURL(URL));
    }


}
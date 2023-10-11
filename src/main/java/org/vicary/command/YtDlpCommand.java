package org.vicary.command;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.vicary.configuration.BotInfo;
import org.vicary.model.FileResponse;

@Configuration
@ConfigurationProperties("yt-dlp-commands")
public class YtDlpCommand {
    private final static String YT_DLP = "yt-dlp";
    private final static String AUDIO_ONLY = "-x";
    private final static String AUDIO_FORMAT = "--audio-format";
    private final static String AUDIO_QUALITY = "--audio-quality";
    private final static String PATH = "-o";
    private final static String THUMBNAIL_LINK = "https://i.ytimg.com/vi/";
    private final static String THUMBNAIL_TYPE = "/mqdefault.jpg";
    private final static String youtubeUrl = "https://youtu.be/";
    private final static String EMBED_THUMBNAIL = "--embed-thumbnail";
    private final static String MAX_FILE_SIZE = "--max-filesize";
    private final static String MAX_FILE_SIZE_IN_MB = "47M";
    private final static String FILE_INFO = "-j";
    private final static String NETRC = "--netrc";
    private final static String PLAYLIST_ITEMS = "--playlist-items";


    public String[] downloadYouTube(String fileName, FileResponse response) {
        return new String[]{
                YT_DLP,
                AUDIO_ONLY,
                AUDIO_FORMAT,
                response.getExtension().equals("ogg") ? "vorbis" : response.getExtension(),
                AUDIO_QUALITY,
                response.isPremium() ? "0" : "5",
                EMBED_THUMBNAIL,
                MAX_FILE_SIZE,
                MAX_FILE_SIZE_IN_MB,
                PATH,
                fileName,
                youtubeUrl + response.getServiceId()};
    }

    public String[] downloadTwitter(String fileName, FileResponse response) {
        if (response.getExtension().equals("mp4")) {
            return new String[]{
                    YT_DLP,
                    NETRC,
                    PLAYLIST_ITEMS,
                    String.valueOf(response.getMultiVideoNumber() == 0 ? 1 : response.getMultiVideoNumber()),
                    EMBED_THUMBNAIL,
                    MAX_FILE_SIZE,
                    MAX_FILE_SIZE_IN_MB,
                    PATH,
                    fileName,
                    response.getURL()};
        } else {
            return new String[]{
                    YT_DLP,
                    NETRC,
                    PLAYLIST_ITEMS,
                    String.valueOf(response.getMultiVideoNumber() == 0 ? 1 : response.getMultiVideoNumber()),
                    AUDIO_ONLY,
                    AUDIO_FORMAT,
                    response.getExtension().equals("ogg") ? "vorbis" : response.getExtension(),
                    AUDIO_QUALITY,
                    response.isPremium() ? "0" : "5",
                    EMBED_THUMBNAIL,
                    MAX_FILE_SIZE,
                    MAX_FILE_SIZE_IN_MB,
                    PATH,
                    fileName,
                    response.getURL()};
        }
    }

    public String[] downloadTikTok(String fileName, FileResponse response) {
        if (response.getExtension().equals("mp4")) {
            return new String[]{
                    YT_DLP,
                    EMBED_THUMBNAIL,
                    MAX_FILE_SIZE,
                    MAX_FILE_SIZE_IN_MB,
                    PATH,
                    fileName,
                    response.getURL()};
        } else {
            return new String[]{
                    YT_DLP,
                    AUDIO_ONLY,
                    AUDIO_FORMAT,
                    response.getExtension().equals("ogg") ? "vorbis" : response.getExtension(),
                    AUDIO_QUALITY,
                    response.isPremium() ? "0" : "5",
                    EMBED_THUMBNAIL,
                    MAX_FILE_SIZE,
                    MAX_FILE_SIZE_IN_MB,
                    PATH,
                    fileName,
                    response.getURL()};
        }
    }


    public String[] downloadSoundCloud(String fileName, FileResponse response) {
        return new String[]{
                YT_DLP,
                AUDIO_ONLY,
                AUDIO_FORMAT,
                response.getExtension().equals("ogg") ? "vorbis" : response.getExtension(),
                AUDIO_QUALITY,
                response.isPremium() ? "0" : "5",
                PLAYLIST_ITEMS,
                String.valueOf(response.getMultiVideoNumber() == 0 ? 1 : response.getMultiVideoNumber()),
                EMBED_THUMBNAIL,
                MAX_FILE_SIZE,
                MAX_FILE_SIZE_IN_MB,
                PATH,
                fileName,
                response.getURL()};
    }

    public String[] downloadInstagram(String fileName, FileResponse response) {
        if (response.getExtension().equals("mp4")) {
            return new String[]{
                    YT_DLP,
                    PLAYLIST_ITEMS,
                    String.valueOf(response.getMultiVideoNumber() == 0 ? 1 : response.getMultiVideoNumber()),
                    EMBED_THUMBNAIL,
                    MAX_FILE_SIZE,
                    MAX_FILE_SIZE_IN_MB,
                    PATH,
                    fileName,
                    response.getURL()};
        } else {
            return new String[]{
                    YT_DLP,
                    PLAYLIST_ITEMS,
                    String.valueOf(response.getMultiVideoNumber() == 0 ? 1 : response.getMultiVideoNumber()),
                    AUDIO_ONLY,
                    AUDIO_FORMAT,
                    response.getExtension().equals("ogg") ? "vorbis" : response.getExtension(),
                    AUDIO_QUALITY,
                    response.isPremium() ? "0" : "5",
                    EMBED_THUMBNAIL,
                    MAX_FILE_SIZE,
                    MAX_FILE_SIZE_IN_MB,
                    PATH,
                    fileName,
                    response.getURL()};
        }
    }

    public String[] downloadThumbnailYoutube(String thumbnailName, String youtubeId) {
        return new String[]{
                YT_DLP,
                PATH,
                thumbnailName,
                THUMBNAIL_LINK + youtubeId + THUMBNAIL_TYPE};
    }

    public String[] downloadThumbnailSoundCloud(String thumbnailName, String thumbnailURL) {
        return new String[]{
                YT_DLP,
                PATH,
                thumbnailName,
                thumbnailURL};
    }

    public String[] fileInfoInstagram(String URL) {
        return new String[]{
                YT_DLP,
                FILE_INFO,
                URL};
    }

    public String[] fileInfoTikTok(String URL) {
        return new String[]{
                YT_DLP,
                FILE_INFO,
                URL};
    }

    public String[] fileInfoSoundCloud(String URL) {
        return new String[]{
                YT_DLP,
                FILE_INFO,
                URL};
    }

    public String[] fileInfoYouTube(String youtubeId) {
        return new String[]{
                YT_DLP,
                FILE_INFO,
                youtubeUrl + youtubeId};
    }

    public String[] fileInfoTwitter(String URL) {
        return new String[]{
                YT_DLP,
                NETRC,
                FILE_INFO,
                URL};
    }

    public String getDownloadDestination() {
        return BotInfo.getDownloadDestination();
    }
}

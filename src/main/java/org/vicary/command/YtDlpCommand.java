package org.vicary.command;

import lombok.Getter;
import org.springframework.stereotype.Component;
import org.vicary.model.FileResponse;

@Component
public class YtDlpCommand {
    private final static String YT_DLP = "yt-dlp";
    private final static String FILE_EXTENSION = "-x";
    private final static String AUDIO_FORMAT = "--audio-format";
    private final static String AUDIO_QUALITY = "--audio-quality";
    private final static String PATH = "-o";
    private final static String THUMBNAIL_LINK = "https://i.ytimg.com/vi/";
    private final static String THUMBNAIL_TYPE = "/mqdefault.jpg";
    private final static String youtubeUrl = "https://youtu.be/";
    private final static String EMBED_THUMBNAIL = "--embed-thumbnail";
    private final static String MAX_FILE_SIZE = "--max-filesize";
    private final static String MAX_FILE_SIZE_IN_MB = "45M";
    private final static String FILE_INFO = "-j";
    private final static String NETRC = "--netrc";
    private final String PLAYLIST_ITEMS = "--playlist-items";
    @Getter
    private final String downloadDestination = "/Users/vicary/desktop/folder/";


    public String[] getDownloadYouTubeFile(String fileName, String youtubeId, String extension, boolean premium) {
        return new String[]{
                YT_DLP,
                FILE_EXTENSION,
                AUDIO_FORMAT,
                extension,
                AUDIO_QUALITY,
                premium ? "0" : "5",
                EMBED_THUMBNAIL,
                MAX_FILE_SIZE,
                MAX_FILE_SIZE_IN_MB,
                PATH,
                fileName,
                youtubeUrl + youtubeId};
    }

    public String[] getDownloadTwitterFile(String fileName, String URL, int multiVideoNumber) {
        return new String[]{
                YT_DLP,
                NETRC,
                PLAYLIST_ITEMS,
                String.valueOf(multiVideoNumber == 0 ? 1 : multiVideoNumber),
                EMBED_THUMBNAIL,
                MAX_FILE_SIZE,
                MAX_FILE_SIZE_IN_MB,
                PATH,
                fileName,
                URL};
    }

    public String[] getDownloadTikTokFile(String fileName, String URL) {
        return new String[]{
                YT_DLP,
                EMBED_THUMBNAIL,
                MAX_FILE_SIZE,
                MAX_FILE_SIZE_IN_MB,
                PATH,
                fileName,
                URL};
    }

    public String[] getDownloadInstagramFile(String fileName, String URL, int multiVideoNumber) {
        return new String[]{
                YT_DLP,
                PLAYLIST_ITEMS,
                String.valueOf(multiVideoNumber == 0 ? 1 : multiVideoNumber),
                EMBED_THUMBNAIL,
                MAX_FILE_SIZE,
                MAX_FILE_SIZE_IN_MB,
                PATH,
                fileName,
                URL};
    }

    public String[] getDownloadYouTubeThumbnail(String thumbnailName, String youtubeId) {
        return new String[]{
                YT_DLP,
                PATH,
                thumbnailName,
                THUMBNAIL_LINK + youtubeId + THUMBNAIL_TYPE};
    }

    public String[] getDownloadFileInfo(String URL) {
        return new String[]{
                YT_DLP,
                FILE_INFO,
                URL};
    }

    public String[] getDownloadFileInfoTwitter(String URL) {
        return new String[]{
                YT_DLP,
                NETRC,
                FILE_INFO,
                URL};
    }
}

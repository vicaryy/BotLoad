package org.vicary.command;

import lombok.Getter;
import org.springframework.stereotype.Component;
import org.vicary.model.twitter.TwitterFileResponse;

@Component
public class TwitterCommand {
    private final String ytDlpCommand = "yt-dlp";
    private final String pathCommand = "-o";
    @Getter
    private final String path = "/Users/vicary/desktop/folder/";
    private final String thumbnailLink = "https://i.ytimg.com/vi/";
    private final String thumbnailType = "/mqdefault.jpg";
    private final String embedThumbnailCommand = "--embed-thumbnail";
    private final String maxFileSizeCommand = "--max-filesize";
    private final String maxFileSize = "45M";
    private final String renameCommand = "mv";
    private final String netrcCommand = "--netrc";
    private final String fileInfoCommand = "-j";


    public String[] downloadFile(TwitterFileResponse response) {
        String extension = response.getExtension();
        String filePath = String.format("%s%s.%s", path, response.getTitle(), extension);

        String[] command = {ytDlpCommand, netrcCommand, embedThumbnailCommand, maxFileSizeCommand, maxFileSize, pathCommand, filePath, response.getUrl()};
        return command;
    }

    public String[] downloadThumbnail(String thumbnailName, String youtubeId) {
        String[] command = {ytDlpCommand, pathCommand, path + thumbnailName, thumbnailLink + youtubeId + thumbnailType};
        return command;
    }

    public String[] downloadFileInfo(String twitterUrl) {
        String[] command = {ytDlpCommand, netrcCommand, fileInfoCommand, twitterUrl};
        return command;
    }
}

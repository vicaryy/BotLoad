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
    private final String embedThumbnailCommand = "--embed-thumbnail";
    private final String maxFileSizeCommand = "--max-filesize";
    private final String maxFileSize = "45M";
    private final String netrcCommand = "--netrc";
    private final String fileInfoCommand = "-j";
    private final String multiVideoNumberCommand = "--playlist-items";


    public String[] downloadFile(String url, String fileName, int multiVideoNumber) {
        multiVideoNumber = multiVideoNumber == 0 ? 1 : multiVideoNumber;
        String[] command = {ytDlpCommand, netrcCommand, multiVideoNumberCommand, String.valueOf(multiVideoNumber), embedThumbnailCommand, maxFileSizeCommand, maxFileSize, pathCommand, fileName, url};
        return command;
    }

    public String[] downloadFileInfo(String twitterUrl) {
        String[] command = {ytDlpCommand, netrcCommand, fileInfoCommand, twitterUrl};
        return command;
    }
}

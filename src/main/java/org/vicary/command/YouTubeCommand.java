package org.vicary.command;

import lombok.Getter;
import org.springframework.stereotype.Component;
import org.vicary.model.youtube.YouTubeFileResponse;

@Component
public class YouTubeCommand {
    private final String ytDlpCommand = "yt-dlp";
    private final String fileExtensionCommand = "-x";
    private final String audioFormatCommand = "--audio-format";
    private final String audioQualityCommand = "--audio-quality";
    private final String pathCommand = "-o";
    @Getter
    private final String path = "/Users/vicary/desktop/folder/";
    private final String defaultFileName = "%(title)s.%(ext)s";
    private final String thumbnailLink = "https://i.ytimg.com/vi/";
    private final String thumbnailType = "/mqdefault.jpg";
    private final String youtubeUrl = "https://youtu.be/";
    private final String embedThumbnailCommand = "--embed-thumbnail";
    private final String maxFileSizeCommand = "--max-filesize";
    private final String maxFileSize = "45M";
    private final String deleteCommand = "rm";
    private final String renameCommand = "mv";
    private final String fileInfoCommand = "-j";


    public String[] getFileCommand(YouTubeFileResponse response) {
        boolean premium = response.getPremium();
        String quality = premium ? "0" : "5";
        String extension = response.getExtension();
        String filePath = String.format("%s%s.%s", path, response.getTitle(), extension);
        String youtubeId = response.getYoutubeId();

        String[] command = {ytDlpCommand, fileExtensionCommand, audioFormatCommand, extension, audioQualityCommand, quality, embedThumbnailCommand, maxFileSizeCommand, maxFileSize, pathCommand, filePath, youtubeUrl + youtubeId};
        return command;
    }

    public String[] getThumbnailCommand(String thumbnailName, String youtubeId) {
        String[] command = {ytDlpCommand, pathCommand, path + thumbnailName, thumbnailLink + youtubeId + thumbnailType};
        return command;
    }

    public String[] getFileInfoCommand(String youtubeId){
        String[] command = {ytDlpCommand, fileInfoCommand, youtubeUrl + youtubeId};
        return command;
    }
}

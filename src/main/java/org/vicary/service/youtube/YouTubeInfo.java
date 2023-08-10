package org.vicary.service.youtube;

import lombok.Getter;
import org.springframework.stereotype.Component;
import org.vicary.format.MarkdownV2;

@Getter
@Component
public class YouTubeInfo {
    private final String connectingToYoutubeInfo = MarkdownV2.apply("Connecting to YouTube...").toItalic().newlineBefore().get();
    private final String fileDownloadingInfo = MarkdownV2.apply("Downloading file... [0.0%]").toItalic().newlineBefore().get();
    private final String thumbDownloadingInfo = MarkdownV2.apply("Downloading thumbnail... [0.0%]").toItalic().newlineBefore().get();
    private final String fileTooBigInfo = MarkdownV2.apply("File is too big!").toBold().newlineAfter().newlineAfter().get();
    private final String upTo50MBInfo = MarkdownV2.apply("Sorry but i can upload files up to 50MB (Telegrams fault).\nThings may change in time.").get();
    private String convertingInfo;
    private final String renamingInfo = MarkdownV2.apply("Renaming...").toItalic().newlineBefore().get();

    public String getConvertingInfo(String extension) {
        convertingInfo = MarkdownV2.apply(String.format("Converting to %s...", extension)).toItalic().newlineBefore().get();
        return convertingInfo;
    }
}

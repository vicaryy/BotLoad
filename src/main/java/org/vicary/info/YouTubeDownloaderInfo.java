package org.vicary.info;

import lombok.Getter;
import org.springframework.stereotype.Component;
import org.vicary.format.MarkdownV2;

@Getter
@Component
public class YouTubeDownloaderInfo {
    private final String connectingToYoutube = MarkdownV2.apply("Connecting to YouTube...").toItalic().newlineBefore().get();
    private final String fileDownloading = MarkdownV2.apply("Downloading file... [0.0%]").toItalic().newlineBefore().get();
    private final String thumbnailDownloading = MarkdownV2.apply("Downloading thumbnail... [0.0%]").toItalic().newlineBefore().get();
    private final String fileTooBig = MarkdownV2.apply("File is too big!").toBold().newlineAfter().newlineAfter().get();
    private final String upTo50Mb = MarkdownV2.apply("Sorry but i can upload files up to 50MB (Telegrams fault).\nThings may change in time.").get();
    private String converting;
    private final String renaming = MarkdownV2.apply("Renaming...").toItalic().newlineBefore().get();

    public String getConverting(String extension) {
        converting = MarkdownV2.apply(String.format("Converting to %s...", extension)).toItalic().newlineBefore().get();
        return converting;
    }
}

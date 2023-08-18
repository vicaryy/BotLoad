package org.vicary.info;

import lombok.Getter;
import org.springframework.stereotype.Component;
import org.vicary.format.MarkdownV2;

@Getter
@Component
public class TwitterDownloaderInfo {
    private final String connectingToTwitter = MarkdownV2.apply("Connecting to Twitter...").toItalic().newlineBefore().get();
    private final String fileDownloading = MarkdownV2.apply("Downloading file... [0.0%]").toItalic().newlineBefore().get();
    private final String thumbnailDownloading = MarkdownV2.apply("Downloading thumbnail... [0.0%]").toItalic().newlineBefore().get();
    private final String fileTooBig = MarkdownV2.apply("File is too big!").toBold().newlineAfter(2).get();
    private final String fileTooBigExplanation = MarkdownV2.apply("Sorry but i can upload files up to 50MB (Telegrams fault).\nThings may change in time.").get();
    private final String renaming = MarkdownV2.apply("Renaming...").toItalic().newlineBefore().get();
    private final String noVideo = MarkdownV2.apply("No video!").toBold().newlineAfter(2).get();
    private final String noVideoExplanation = MarkdownV2.apply("Sorry but i can't find video in your link.").get();
    private final String errorInDownloading = MarkdownV2.apply("Sorry but i can't download this file.").toBold().newlineAfter().get();
    private final String tryAgainLater = MarkdownV2.apply("Try again later.").get();
}

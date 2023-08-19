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
    private final String multiVideo = MarkdownV2.apply("Multi-video Twitter link.").toBold().newlineAfter(2).get();
    private final String multiVideoExplanation = MarkdownV2.apply("This Twitter link contains multiple videos, you need to specify which one you want to download." +
                                                                  "\nFor example: https://twitter.com/Example/link/1685062660365377537 @3" +
                                                                  "\nWhere '@3' means that the third video has to be downloaded." +
                                                                  "\nPlease try again.").get();
    private final String multiVideoAmountTooHigh = MarkdownV2.apply("Too much videos!").toBold().newlineAfter(2).get();
    private final String multiVideoAmountTooHighExplanation = MarkdownV2.apply("Sorry but this multi-video Twitter link has too much videos, more than my limit - 15.\nTry another link.").get();
}

package org.vicary.info;

import lombok.Getter;
import org.springframework.stereotype.Component;
import org.vicary.format.MarkdownV2;

@Component
@Getter
public class DownloaderInfo {
    private final String connectingToYoutube = MarkdownV2.apply("Connecting to YouTube...").toItalic().newlineBefore().get();

    private final String connectingToTwitter = MarkdownV2.apply("Connecting to Twitter...").toItalic().newlineBefore().get();

    private final String connectingToTikTok = MarkdownV2.apply("Connecting to TikTok...").toItalic().newlineBefore().get();

    private final String connectingToInstagram = MarkdownV2.apply("Connecting to Instagram...").toItalic().newlineBefore().get();

    private final String fileDownloading = MarkdownV2.apply("Downloading file... [0.0%]").toItalic().newlineBefore().get();

    private final String thumbnailDownloading = MarkdownV2.apply("Downloading thumbnail... [0.0%]").toItalic().newlineBefore().get();

    private final String fileTooBig = MarkdownV2.apply("File is too big!").toBold().newlineAfter(2).get()
                                      + MarkdownV2.apply("Sorry but i can upload files up to 50MB (Telegrams fault).\nThings may change in time.").get();

    private final String renaming = MarkdownV2.apply("Renaming...").toItalic().newlineBefore().get();

    private final String noVideo = MarkdownV2.apply("No video!").toBold().newlineAfter(2).get()
                                   + MarkdownV2.apply("Sorry but i can't find video in your link.").get();

    private final String errorInDownloading = MarkdownV2.apply("Sorry but i can't download this file.").toBold().newlineAfter().get()
                                              + MarkdownV2.apply("Try again later.").get();

    private final String multiVideo = MarkdownV2.apply("Multi-video link.").toBold().newlineAfter(2).get()
                                      + MarkdownV2.apply("This link contains multiple videos, you need to specify which one you want to download." +
                                                         "\n\nFor example: \nhttps://example.com/link/133742069 #3" +
                                                         "\nWhere '#3' means that the third video has to be downloaded." +
                                                         "\n\nPlease try again.").get();

    private final String multiVideoAmountTooHigh = MarkdownV2.apply("Too much videos!").toBold().newlineAfter(2).get()
                                                   + MarkdownV2.apply("Sorry but this multi-video link has too much videos, more than my limit - 15.\nTry another link.").get();

    public String getConverting(String extension) {
        return MarkdownV2.apply(String.format("Converting to %s...", extension)).toItalic().newlineBefore().get();
    }

    public String getReceivedWrongNumberInMultiVideo(int amountOfFiles, int specifyNumber) {
        return MarkdownV2.apply("No video in multi-video link!").toBold().newlineAfter(2).get()
               + MarkdownV2.apply(String.format("Amount of videos in your link: %d" +
                                                "\nThe video number you have specified: %d", amountOfFiles, specifyNumber)).newlineAfter().get();
    }
}

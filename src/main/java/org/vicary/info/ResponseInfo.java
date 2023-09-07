package org.vicary.info;

import lombok.Getter;
import org.springframework.stereotype.Component;
import org.vicary.format.MarkdownV2;

@Component
@Getter
public class ResponseInfo {
    private final String gotTheLink = MarkdownV2.apply("Got the link!").toBold().newlineAfter().get();

    private final String holdOn = MarkdownV2.apply("Just hold on for a moment.").newlineAfter().get();

    private final String sending = MarkdownV2.apply("Sending...").toItalic().newlineBefore().get();

    private final String error = MarkdownV2.apply("I'm sorry but something goes wrong.").toBold().get();

    private final String hellaBigNumber = MarkdownV2.apply("Oh come on, that's hella big number.").toBold().get();

    private final String wrongExtension = MarkdownV2.apply("I'm sorry but I don't support this extension.").get();

    private final String wrongMultiVideoNumber = MarkdownV2.apply("I'm sorry but your multi-video number is invalid,\n please try again.").get();

    private final String received = "Here's your file";

    private final String title = MarkdownV2.apply("Title: ").toBold().newlineBefore().get();

    private final String artist = MarkdownV2.apply("Artist: ").toBold().newlineBefore().get();

    private final String track = MarkdownV2.apply("Track: ").toBold().newlineBefore().get();

    private final String album = MarkdownV2.apply("Album: ").toBold().newlineBefore().get();

    private final String releaseYear = MarkdownV2.apply("Release year: ").toBold().newlineBefore().get();

    private final String duration = MarkdownV2.apply("Duration: ").toBold().newlineBefore().get();

    private final String size = MarkdownV2.apply("Size: ").toBold().newlineBefore().get();

    private final String extension = MarkdownV2.apply("Extension: ").toBold().newlineBefore().get();

    private final String quality = MarkdownV2.apply("Quality: ").toBold().newlineBefore().get();
}

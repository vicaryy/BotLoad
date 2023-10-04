package org.vicary.service.response;

import org.vicary.format.MarkdownV2;

public class CommandResponse {
    public void response(String text) {
        if (text.equals("/start")) {

        } else if (text.equals("/help")) {

        } else if (text.equals("/tip")) {

        } else if (text.equals("/extensions")) {

        }
    }

    //I can download files from:
    //YouTube, Twitter, TikTok, SoundCloud, and Instagram.
    //If you want to download a file, just paste the link, and I'll recognize the service and download the video or audio from it.
    //You can specify the file extension yourself using the shortcut: -ext "EXTENSION_NAME"
    //To learn more about extensions, you can use the command /extensions.
    //You can add ID3Tags to an MP3 audio file using a shortcut and separating the tags with colons: -tag artist:title:album:releaseYear:genre
    //Sample bot usage:
    //If you need additional assistance, use the command /help.
    //If you want to support me, use the command /tip

    public String getStart() {
        return MarkdownV2.apply("Hello!").newlineAfter().get()
               + MarkdownV2.apply("I'm your ").get() + MarkdownV2.apply("Downloader Bot.").toBold().newlineAfter().get()
               + MarkdownV2.apply("I can download files from:")
    }
}






















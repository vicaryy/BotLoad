package org.vicary.service.response;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.vicary.format.MarkdownV2;
import org.vicary.service.quick_sender.QuickSender;

@Component
@RequiredArgsConstructor
public class CommandResponse {

    private final QuickSender quickSender;

    private final static String START = MarkdownV2.apply("Hello! 👋").newlineAfter(2).get()

                                        + MarkdownV2.apply("I can download files from: ").toBold().newlineAfter().get()
                                        + MarkdownV2.apply("YouTube, Twitter, TikTok, SoundCloud, and Instagram.").newlineAfter(2).get()

                                        + MarkdownV2.apply("Additional functions:").toBold().newlineAfter().get()
                                        + MarkdownV2.apply("You can specify the extension using: ").newlineAfter().get()
                                        + MarkdownV2.apply("-ext mp3").newlineAfter(2).get()

                                        + MarkdownV2.apply("You can add ID3Tags using: ").newlineAfter().get()
                                        + MarkdownV2.apply("-tag artist:title:album:releaseYear:genre").newlineAfter(2).get()


                                        + MarkdownV2.apply("Sample bot usage:").toBold().newlineAfter().get()
                                        + MarkdownV2.apply("twitter.com/sample -ext mp3 -tag Flume:Go:Palaces:2022:Electronic").newlineAfter(2).get()

                                        + MarkdownV2.apply("More commands:").toBold().newlineAfter().get()
                                        + MarkdownV2.apply("/limits").toItalic().get() + MarkdownV2.apply(" - more about limits ☝️").newlineAfter().get()
                                        + MarkdownV2.apply("/extensions").toItalic().get() + MarkdownV2.apply(" - more about extensions 🔑").newlineAfter().get()
                                        + MarkdownV2.apply("/help").toItalic().get() + MarkdownV2.apply(" - if you need more help 🛟").newlineAfter().get()
                                        + MarkdownV2.apply("/tip").toItalic().get() + MarkdownV2.apply(" - if you want to support me ☕️").get();

    private final static String HELP = MarkdownV2.apply("More help 🛟").toBold().newlineAfter(2).get()
                                       + MarkdownV2.apply("For more help you can contact the administrator: ").get() + MarkdownV2.apply("@vicary1").toItalic().get();

    private final static String TIP = MarkdownV2.apply("Want to support me? ☕️").toBold().newlineAfter(2).get()
                                      + MarkdownV2.apply("BTC address: 17PkbNkE1FfCcyWwJLWMkthaHTRfvBbLtT").newlineAfter().get()
                                      + MarkdownV2.apply("ETH address: 0x2ac2cc2fc09fcb051a928c7f7dcb6c332a2e73ac").newlineAfter(2).get()
                                      + MarkdownV2.apply("Thank you! 💞").get();

    private final static String EXTENSIONS = MarkdownV2.apply("Extensions 🔑").toBold().newlineAfter(2).get()

                                             + MarkdownV2.apply("YouTube").toBold().newlineAfter().get()
                                             + MarkdownV2.apply("Default extension: mp3").newlineAfter().get()
                                             + MarkdownV2.apply("Available extensions: m4a, flac, wav, (mp4 in the future)").newlineAfter(2).get()

                                             + MarkdownV2.apply("Soundcloud").toBold().newlineAfter().get()
                                             + MarkdownV2.apply("Default extension: mp3").newlineAfter().get()
                                             + MarkdownV2.apply("Available extensions: m4a, flac, wav").newlineAfter(2).get()

                                             + MarkdownV2.apply("Twitter").toBold().newlineAfter().get()
                                             + MarkdownV2.apply("Default extension: mp4").newlineAfter().get()
                                             + MarkdownV2.apply("Available extensions: mp3, m4a, flac, wav").newlineAfter(2).get()

                                             + MarkdownV2.apply("TikTok").toBold().newlineAfter().get()
                                             + MarkdownV2.apply("Default extension: mp4").newlineAfter().get()
                                             + MarkdownV2.apply("Available extensions: mp3, m4a, flac, wav").newlineAfter(2).get()

                                             + MarkdownV2.apply("Instagram").toBold().newlineAfter().get()
                                             + MarkdownV2.apply("Default extension: mp4").newlineAfter().get()
                                             + MarkdownV2.apply("Available extensions: mp3, m4a, flac, wav").newlineAfter(2).get();

    private final static String LIMITS = MarkdownV2.apply("Limits ☝️").toBold().newlineAfter(2).get()
                                         + MarkdownV2.apply("Due to Telegram's limitations, I can send files up to 50MB but this will change in the future.").get();


    public void response(String text, String chatId) {
        String message = null;
        if (text.equals("/start")) {
            message = START;
        } else if (text.equals("/help")) {
            message = HELP;
        } else if (text.equals("/tip")) {
            message = TIP;
        } else if (text.equals("/extensions")) {
            message = EXTENSIONS;
        } else if (text.equals("/limits")) {
            message = LIMITS;
        }

        if (message != null)
            quickSender.message(chatId, message, true);
    }
}






















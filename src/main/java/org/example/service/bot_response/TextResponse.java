package org.example.service.bot_response;

import lombok.RequiredArgsConstructor;
import org.example.api_object.Update;
import org.example.api_request.send.SendMessage;
import org.example.service.RequestService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TextResponse {
    private final RequestService service;

    public void response(Update update) {
        String text = update.getMessage().getText().toLowerCase();
        String chatId = update.getChatId();
        String response = null;
        SendMessage sendMessage;
        text = text.toLowerCase();
        if (text.equals("siema") ||
                text.equals("hej") ||
                text.equals("yo") ||
                text.equals("cześć") ||
                text.equals("witam") ||
                text.equals("dzień dobry") ||
                text.equals("cze")) {
            response = "Cześć " + update.getMessage().getFrom().getFirstName() + " w czym mogę ci pomóc? :)";
        }
        if (text.equals("co tam") ||
                text.equals("co tam?") ||
                text.equals("co słychać?") ||
                text.equals("co slychac?") ||
                text.equals("co slychac") ||
                text.equals("jak tam?") ||
                text.equals("jak tam") ||
                text.equals("co u ciebie") ||
                text.equals("co u ciebie?")) {
            response = "Wszystko u mnie w porządku, a u ciebie?";
        }

        if (response == null) response = "I don't know what you mean by " + text;
        sendMessage = new SendMessage(chatId, response);

        try {
            service.sendRequest(sendMessage);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

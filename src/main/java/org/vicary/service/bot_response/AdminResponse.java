package org.vicary.service.bot_response;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.vicary.api_object.Update;
import org.vicary.api_request.send.SendMessage;
import org.vicary.service.RequestService;
import org.vicary.service.UserService;

@Service
@RequiredArgsConstructor
public class AdminResponse {

    private final UserService userService;

    private final RequestService requestService;

    public void response(Update update) {
        String text = update.getMessage().getText();
        String chatId = update.getChatId();
        if (text.startsWith("/set premium"))
            setPremium(text, chatId);
        if (text.startsWith("/set standard")) {
            setStandard(text, chatId);
        }
    }

    public void setPremium(String text, String chatId) {
        String[] premiums = text.split(" ");
        if (premiums.length > 1) {
            if (userService.updateUserToPremiumByNick(premiums[2])) {
                try {
                    requestService.sendRequestAsync(SendMessage.builder().chatId(chatId).text("Done.").build());
                } catch (Exception ignored) {
                }
            }
        }
    }

    public void setStandard(String text, String chatId) {
        String[] premiums = text.split(" ");
        if (premiums.length > 1) {
            if (userService.updateUserToPremiumByNick(premiums[2])) {
                try {
                    requestService.sendRequestAsync(SendMessage.builder().chatId(chatId).text("Done.").build());
                } catch (Exception ignored) {
                }
            }
        }
    }
}


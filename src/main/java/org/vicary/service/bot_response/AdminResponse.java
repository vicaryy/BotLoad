package org.vicary.service.bot_response;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.vicary.api_object.Update;
import org.vicary.service.UserService;
import org.vicary.service.quick_sender.QuickSender;

@Service
@RequiredArgsConstructor
public class AdminResponse {

    private final UserService userService;

    private final QuickSender quickSender;

    public void response(Update update) {
        String text = update.getMessage().getText();
        String chatId = update.getChatId();
        if (text.startsWith("/set premium"))
            setPremium(text, chatId);
        if (text.startsWith("/set standard"))
            setStandard(text, chatId);
        if (text.startsWith("/set admin"))
            setAdmin(text, chatId);
        if (text.startsWith("/set non-admin"))
            setNonAdmin(text, chatId);
    }

    public void setPremium(String text, String chatId) {
        String[] premiums = text.split(" ");
        if (premiums.length > 1) {
            String userNick = premiums[2];
            if (userService.updateUserToPremiumByNick(userNick))
                quickSender.message(chatId, String.format("User %s has been updated to Premium.", premiums[2]), false);
        }
    }

    public void setStandard(String text, String chatId) {
        String[] premiums = text.split(" ");
        if (premiums.length > 1) {
            String userNick = premiums[2];
            if (userService.updateUserToStandardByNick(userNick))
                quickSender.message(chatId, String.format("User %s has been updated to Standard.", premiums[2]), false);
        }
    }

    public void setAdmin(String text, String chatId) {
        String[] premiums = text.split(" ");
        if (premiums.length > 1) {
            String userNick = premiums[2];
            if (userService.updateUserToAdminByNick(userNick))
                quickSender.message(chatId, String.format("User %s has been updated to Admin.", premiums[2]), false);
        }
    }

    public void setNonAdmin(String text, String chatId) {
        String[] premiums = text.split(" ");
        if (premiums.length > 1) {
            String userNick = premiums[2];
            if (userService.updateUserToNonAdminByNick(userNick))
                quickSender.message(chatId, String.format("User %s has been updated to Non-Admin.", premiums[2]), false);
        }
    }
}


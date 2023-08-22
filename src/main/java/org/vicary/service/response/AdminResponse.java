package org.vicary.service.response;

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

    public void response(String text, String chatId) {
        if (text.startsWith("/set premium"))
            setPremium(getUserNick(text), chatId);
        else if (text.startsWith("/set standard"))
            setStandard(getUserNick(text), chatId);
        else if (text.startsWith("/set admin"))
            setAdmin(getUserNick(text), chatId);
        else if (text.startsWith("/set non-admin"))
            setNonAdmin(getUserNick(text), chatId);
    }

    public void setPremium(String userNick, String chatId) {
        if (userService.updateUserToPremiumByNick(userNick))
            quickSender.message(chatId, String.format("User %s successfully updated to Premium.", userNick), false);
        else
            quickSender.message(chatId, String.format("User %s does not exist.", userNick), false);
    }

    public void setStandard(String userNick, String chatId) {
        if (userService.updateUserToStandardByNick(userNick))
            quickSender.message(chatId, String.format("User %s successfully updated to Standard.", userNick), false);
        else
            quickSender.message(chatId, String.format("User %s does not exist.", userNick), false);
    }

    public void setAdmin(String userNick, String chatId) {
        if (userService.updateUserToAdminByNick(userNick))
            quickSender.message(chatId, String.format("User %s successfully updated to Admin.", userNick), false);
        else
            quickSender.message(chatId, String.format("User %s does not exist.", userNick), false);
    }

    public void setNonAdmin(String userNick, String chatId) {
        if (userService.updateUserToNonAdminByNick(userNick))
            quickSender.message(chatId, String.format("User %s successfully updated to Non-Admin.", userNick), false);
        else
            quickSender.message(chatId, String.format("User %s does not exist.", userNick), false);
    }

    public String getUserNick(String text) {
        String[] textArray = text.split(" ");
        if (textArray.length > 1) {
            return textArray[2];
        }
        return "";
    }
}


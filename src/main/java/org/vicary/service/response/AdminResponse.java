package org.vicary.service.response;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.vicary.api_object.bot.bot_command.BotCommand;
import org.vicary.api_request.commands.DeleteMyCommands;
import org.vicary.api_request.commands.GetMyCommands;
import org.vicary.api_request.commands.SetMyCommands;
import org.vicary.service.RequestService;
import org.vicary.service.UserService;
import org.vicary.service.quick_sender.QuickSender;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminResponse {

    private final UserService userService;

    private final QuickSender quickSender;

    private final RequestService requestService;

    public void response(String text, String chatId) {
        if (text.startsWith("/set premium "))
            setPremium(removePrefix(text), chatId);

        else if (text.startsWith("/set standard "))
            setStandard(removePrefix(text), chatId);

        else if (text.startsWith("/set admin "))
            setAdmin(removePrefix(text), chatId);

        else if (text.startsWith("/set non-admin "))
            setNonAdmin(removePrefix(text), chatId);

        else if (text.startsWith("/set command "))
            setCommand(removePrefix(text), chatId);

        else if (text.startsWith("/remove command "))
            removeCommand(removePrefix(text), chatId);

        else if (text.startsWith("/remove commands all"))
            removeAllCommands(chatId);
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


    public void setCommand(String text, String chatId) {
        String[] commandAndDescription = text.split(":");

        if (commandAndDescription[0] == null || commandAndDescription[0].isBlank()) {
            quickSender.message(chatId, "Command not found.", false);
            return;
        }

        String command = commandAndDescription[0];
        String description = commandAndDescription.length > 1 ? commandAndDescription[1] : command;
        BotCommand botCommand = BotCommand.builder()
                .command(command)
                .description(description)
                .build();

        List<BotCommand> commandList = requestService.sendRequestList(new GetMyCommands());
        commandList.add(botCommand);

        SetMyCommands setMyCommands = new SetMyCommands(commandList);
        try {
            requestService.sendRequest(setMyCommands);
            quickSender.message(chatId, "Successfully add " + command + " command.", false);
        } catch (Exception ex) {
            quickSender.message(chatId, "Something goes wrong, check your command and try again.", false);
        }
    }

    public void removeCommand(String text, String chatId) {
        if (text.isBlank()) {
            quickSender.message(chatId, "Command not found.", false);
            return;
        }

        String command = text.startsWith("/") ? text.substring(1) : text;
        List<BotCommand> commandList = requestService.sendRequestList(new GetMyCommands());

        for (BotCommand com : commandList) {
            if (com.getCommand().equals(command)) {
                commandList.remove(com);
                SetMyCommands setMyCommands = new SetMyCommands(commandList);
                try {
                    requestService.sendRequest(setMyCommands);
                    quickSender.message(chatId, "Successfully remove " + command + " command.", false);
                } catch (Exception ex) {
                    quickSender.message(chatId, "Something goes wrong, check your command and try again.", false);
                }
                return;
            }
        }

        quickSender.message(chatId, "Command not found.", false);
    }

    public void removeAllCommands(String chatId) {
        requestService.sendRequest(new DeleteMyCommands());
        quickSender.message(chatId, "Successfully removed all commands.", false);
    }


    public String removePrefix(String text) {
        String[] textArray = text.split(" ");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < textArray.length; i++) {
            if (i > 1)
                sb.append(textArray[i]).append(" ");
        }
        return sb.toString().trim();
    }
}


package org.vicary.api_request.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.vicary.api_object.bot.bot_command.*;
import org.vicary.api_request.ApiRequest;
import org.vicary.end_point.EndPoint;

import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class SetMyCommands implements ApiRequest<Boolean> {
    /**
     * Use this method to change the list of the bot's commands.
     *
     * @param commands     A JSON-serialized list of bot commands to be set as the list of the bot's commands. At most 100 commands can be specified.
     * @param scope        A JSON-serialized object, describing the scope of users for which the commands are relevant. Defaults to BotCommandScopeDefault.
     * @param languageCode A two-letter ISO 639-1 language code. If empty, commands will be applied to all users from the given scope, for whose language there are no dedicated commands.
     */
    @NonNull
    @JsonProperty("commands")
    private List<BotCommand> commands;

    @JsonProperty("scope")
    private BotCommandScope scope;

    @JsonProperty("language_code")
    private String languageCode;


    public void setScopeOnDefault() {
        scope = new BotCommandScopeDefault();
    }

    public void setScopeOnAllPrivateChats() {
        scope = new BotCommandScopeAllPrivateChats();
    }

    public void setScopeOnAllGroupChats() {
        scope = new BotCommandScopeAllGroupChats();
    }

    public void setScopeOnAllChatAdministrators() {
        scope = new BotCommandScopeAllChatAdministrators();
    }

    public void setScopeOnChat() {
        scope = new BotCommandScopeChat();
    }

    public void setScopeOnChatAdministrators() {
        scope = new BotCommandScopeChatAdministrators();
    }

    public void setScopeOnChatMember() {
        scope = new BotCommandScopeChatMember();
    }

    @Override
    public Boolean getReturnObject() {
        return true;
    }

    @Override
    public String getEndPoint() {
        return EndPoint.SET_MY_COMMANDS.getPath();
    }

    @Override
    public void checkValidation() {
        if (languageCode == null) languageCode = "";

        if (scope == null) scope = new BotCommandScopeDefault();

        for (BotCommand bot : commands) {
            bot.setCommand(bot.getCommand().toLowerCase());
            if (bot.getCommand().startsWith("/"))
                bot.setCommand(bot.getCommand().substring(1));
        }
    }
}

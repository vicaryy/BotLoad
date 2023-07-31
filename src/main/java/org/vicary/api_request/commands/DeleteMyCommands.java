package org.vicary.api_request.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.vicary.api_object.bot.bot_command.*;
import org.vicary.api_request.ApiRequest;
import org.vicary.end_point.EndPoint;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeleteMyCommands implements ApiRequest<Boolean> {
    /**
     * Use this method to delete the list of the bot's commands for the given scope and user language.
     *
     * @param scope        A JSON-serialized object, describing the scope of users for which the commands are relevant. Defaults to BotCommandScopeDefault.
     * @param languageCode A two-letter ISO 639-1 language code. If empty, commands will be applied to all users from the given scope, for whose language there are no dedicated commands.
     */
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
        return EndPoint.DELETE_MY_COMMANDS.getPath();
    }

    @Override
    public void checkValidation() {
        if(scope == null) scope = new BotCommandScopeDefault();
    }
}

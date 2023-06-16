package org.example.api_object;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class KeyboardButtonRequestChat implements ApiObject {
    @JsonProperty("request_id")
    private Integer requestId;

    @JsonProperty("chat_is_channel")
    private Boolean chatIsChannel;

    @JsonProperty("chat_is_forum")
    private Boolean chatIsForum;

    @JsonProperty("chat_has_username")
    private Boolean chatHasUsername;

    @JsonProperty("chat_is_created")
    private Boolean chatIsCreated;

    @JsonProperty("user_administrator_rights")
    private ChatAdministratorRights userAdministratorRights;

    @JsonProperty("bot_administrator_rights")
    private ChatAdministratorRights botAdministratorRights;

    @JsonProperty("bot_is_member")
    private Boolean botIsMember;

    private KeyboardButtonRequestChat() {
    }
}

package org.example.api_object.keyboard;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.example.api_object.ApiObject;
import org.example.api_object.other.WebAppInfo;

@Getter
public class KeyboardButton implements ApiObject {
    @JsonProperty("text")
    private String text;

    @JsonProperty("request_user")
    private KeyboardButtonRequestUser requestUser;

    @JsonProperty("request_chat")
    private KeyboardButtonRequestChat requestChat;

    @JsonProperty("request_contact")
    private Boolean requestContact;

    @JsonProperty("request_location")
    private Boolean requestLocation;

    @JsonProperty("request_poll")
    private KeyboardButtonPollType requestPoll;

    @JsonProperty("web_app")
    private WebAppInfo webApp;

    private KeyboardButton(){}
}

package org.example.api_object.inline_query.inline_query_result;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.example.api_object.keyboard.InlineKeyboardMarkup;

@Getter
public class InlineQueryResultGame implements InlineQueryResult {
    /**
     * Represents a Game.
     */
    @JsonProperty("type")
    private final String type = "game";

    @JsonProperty("id")
    public String id;

    @JsonProperty("game_short_name")
    public String gameShortName;

    @JsonProperty("reply_markup")
    public InlineKeyboardMarkup replyMarkup;

    private InlineQueryResultGame() {
    }
}

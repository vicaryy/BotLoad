package org.example.api_object.inline_query.inline_query_result;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.example.api_object.keyboard.InlineKeyboardMarkup;

@Getter
@ToString
@EqualsAndHashCode
public class InlineQueryResultGame implements InlineQueryResult {
    /**
     * Represents a Game.
     */
    @JsonProperty("type")
    private final String type = "game";

    @JsonProperty("id")
    private String id;

    @JsonProperty("game_short_name")
    private String gameShortName;

    @JsonProperty("reply_markup")
    private InlineKeyboardMarkup replyMarkup;

    private InlineQueryResultGame() {
    }
}

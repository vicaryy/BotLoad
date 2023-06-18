package org.example.api_object.inline_query.inline_query_result;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.example.api_object.keyboard.InlineKeyboardMarkup;

@Getter
public class InlineQueryResultCachedSticker implements InlineQueryResult {
    /**
     * Represents a link to a sticker stored on the Telegram servers.
     */
    @JsonProperty("type")
    private final String type = "sticker";

    @JsonProperty("id")
    public String id;

    @JsonProperty("sticker_file_id")
    public String stickerFileId;

    @JsonProperty("reply_markup")
    public InlineKeyboardMarkup replyMarkup;

    @JsonProperty("input_message_content")
    public InputMessageContent inputMessageContent;

    private InlineQueryResultCachedSticker() {
    }
}

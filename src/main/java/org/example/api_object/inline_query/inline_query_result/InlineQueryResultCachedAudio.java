package org.example.api_object.inline_query.inline_query_result;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.example.api_object.keyboard.InlineKeyboardMarkup;
import org.example.api_object.message.MessageEntity;

import java.util.List;

@Getter
public class InlineQueryResultCachedAudio implements InlineQueryResult {
    /**
     * Represents a link to an MP3 audio file stored on the Telegram servers.
     */
    @JsonProperty("type")
    private final String type = "audio";

    @JsonProperty("id")
    public String id;

    @JsonProperty("audio_file_id")
    public String audioFileId;

    @JsonProperty("caption")
    public String caption;

    @JsonProperty("parse_mode")
    public String parseMode;

    @JsonProperty("caption_entities")
    public List<MessageEntity> captionEntities;

    @JsonProperty("reply_markup")
    public InlineKeyboardMarkup replyMarkup;

    @JsonProperty("input_message_content")
    public InputMessageContent inputMessageContent;

    private InlineQueryResultCachedAudio() {
    }
}

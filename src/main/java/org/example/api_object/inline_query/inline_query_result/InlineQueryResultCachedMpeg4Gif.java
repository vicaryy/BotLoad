package org.example.api_object.inline_query.inline_query_result;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.example.api_object.keyboard.InlineKeyboardMarkup;
import org.example.api_object.message.MessageEntity;

import java.util.List;

@Getter
public class InlineQueryResultCachedMpeg4Gif implements InlineQueryResult {
    /**
     * Represents a link to a video animation (H.264/MPEG-4 AVC video without sound) stored on the Telegram servers.
     */
    @JsonProperty("type")
    private final String type = "mpeg4_gif";

    @JsonProperty("id")
    public String id;

    @JsonProperty("mpeg4_file_id")
    public String mpeg4FileId;

    @JsonProperty("title")
    public String title;

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

    private InlineQueryResultCachedMpeg4Gif() {
    }
}

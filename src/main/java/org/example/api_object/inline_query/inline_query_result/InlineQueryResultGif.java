package org.example.api_object.inline_query.inline_query_result;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.example.api_object.keyboard.InlineKeyboardMarkup;
import org.example.api_object.message.MessageEntity;

import java.util.List;

@Getter
public class InlineQueryResultGif implements InlineQueryResult {
    /**
     * Represents a link to an animated GIF file. By default, this animated GIF file will be sent by the user
     * with optional caption. Alternatively, you can use input_message_content to send a message with the specified content instead of the animation.
     */
    @JsonProperty("type")
    public String type;

    @JsonProperty("id")
    public String id;

    @JsonProperty("gif_url")
    public String gifUrl;

    @JsonProperty("gif_width")
    public Integer gifWidth;

    @JsonProperty("gif_height")
    public Integer gifHeight;

    @JsonProperty("gif_duration")
    public Integer gifDuration;

    @JsonProperty("thumbnail_url")
    public String thumbnailUrl;

    @JsonProperty("thumbnail_mime_type")
    public String thumbnailMimeType;

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

    private InlineQueryResultGif() {
    }
}

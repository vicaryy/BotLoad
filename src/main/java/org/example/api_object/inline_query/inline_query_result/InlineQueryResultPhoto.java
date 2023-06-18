package org.example.api_object.inline_query.inline_query_result;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.example.api_object.keyboard.InlineKeyboardMarkup;
import org.example.api_object.message.MessageEntity;

import java.util.List;

@Getter
public class InlineQueryResultPhoto implements InlineQueryResult {
    /**
     * Represents a link to a photo. By default, this photo will be sent by the user with optional caption.
     * Alternatively, you can use input_message_content to send a message with the specified content instead of the photo.
     */
    @JsonProperty("type")
    public String type;

    @JsonProperty("id")
    public String id;

    @JsonProperty("photo_url")
    public String photoUrl;

    @JsonProperty("thumbnail_url")
    public String thumbnailUrl;

    @JsonProperty("photo_width")
    public Integer photoWidth;

    @JsonProperty("photo_height")
    public Integer photoHeight;

    @JsonProperty("title")
    public String title;

    @JsonProperty("description")
    public String description;

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

    private InlineQueryResultPhoto() {
    }
}

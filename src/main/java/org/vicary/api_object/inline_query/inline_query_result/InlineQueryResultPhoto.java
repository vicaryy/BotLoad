package org.vicary.api_object.inline_query.inline_query_result;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.vicary.api_object.inline_query.input_message_content.InputMessageContent;
import org.vicary.api_object.keyboard.InlineKeyboardMarkup;
import org.vicary.api_object.message.MessageEntity;

import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
public class InlineQueryResultPhoto implements InlineQueryResult {
    /**
     * Represents a link to a photo. By default, this photo will be sent by the user with optional caption.
     * Alternatively, you can use input_message_content to send a message with the specified content instead of the photo.
     */
    @JsonProperty("type")
    private String type;

    @JsonProperty("id")
    private String id;

    @JsonProperty("photo_url")
    private String photoUrl;

    @JsonProperty("thumbnail_url")
    private String thumbnailUrl;

    @JsonProperty("photo_width")
    private Integer photoWidth;

    @JsonProperty("photo_height")
    private Integer photoHeight;

    @JsonProperty("title")
    private String title;

    @JsonProperty("description")
    private String description;

    @JsonProperty("caption")
    private String caption;

    @JsonProperty("parse_mode")
    private String parseMode;

    @JsonProperty("caption_entities")
    private List<MessageEntity> captionEntities;

    @JsonProperty("reply_markup")
    private InlineKeyboardMarkup replyMarkup;

    @JsonProperty("input_message_content")
    private InputMessageContent inputMessageContent;

    private InlineQueryResultPhoto() {
    }
}

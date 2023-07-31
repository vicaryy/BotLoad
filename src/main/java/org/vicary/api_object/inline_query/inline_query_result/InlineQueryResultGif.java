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
public class InlineQueryResultGif implements InlineQueryResult {
    /**
     * Represents a link to an animated GIF file. By default, this animated GIF file will be sent by the user
     * with optional caption. Alternatively, you can use input_message_content to send a message with the specified content instead of the animation.
     */
    @JsonProperty("type")
    private String type;

    @JsonProperty("id")
    private String id;

    @JsonProperty("gif_url")
    private String gifUrl;

    @JsonProperty("gif_width")
    private Integer gifWidth;

    @JsonProperty("gif_height")
    private Integer gifHeight;

    @JsonProperty("gif_duration")
    private Integer gifDuration;

    @JsonProperty("thumbnail_url")
    private String thumbnailUrl;

    @JsonProperty("thumbnail_mime_type")
    private String thumbnailMimeType;

    @JsonProperty("title")
    private String title;

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

    private InlineQueryResultGif() {
    }
}

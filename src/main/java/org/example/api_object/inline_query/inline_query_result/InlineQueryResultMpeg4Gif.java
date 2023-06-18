package org.example.api_object.inline_query.inline_query_result;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.example.api_object.inline_query.input_message_content.InputMessageContent;
import org.example.api_object.keyboard.InlineKeyboardMarkup;
import org.example.api_object.message.MessageEntity;

import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
public class InlineQueryResultMpeg4Gif implements InlineQueryResult {
    /**
     * Represents a link to a video animation (H.264/MPEG-4 AVC video without sound). By default, this animated MPEG-4 file will be sent by the user
     * with optional caption. Alternatively, you can use input_message_content to send a message with the specified content instead of the animation.
     */
    @JsonProperty("type")
    private String type;

    @JsonProperty("id")
    private String id;

    @JsonProperty("mpeg4_url")
    private String mpeg4Url;

    @JsonProperty("mpeg4_width")
    private Integer mpeg4Width;

    @JsonProperty("mpeg4_height")
    private Integer mpeg4Height;

    @JsonProperty("mpeg4_duration")
    private Integer mpeg4Duration;

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

    private InlineQueryResultMpeg4Gif() {
    }
}

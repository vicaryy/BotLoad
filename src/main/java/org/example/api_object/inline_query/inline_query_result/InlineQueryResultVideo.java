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
public class InlineQueryResultVideo implements InlineQueryResult {
    /**
     * Represents a link to a page containing an embedded video player or a video file. By default, this video file will be sent by the user
     * with an optional caption. Alternatively, you can use input_message_content to send a message with the specified content instead of the video.
     */
    @JsonProperty("type")
    private final String type = "video";

    @JsonProperty("id")
    private String id;

    @JsonProperty("video_url")
    private String videoUrl;

    @JsonProperty("mime_type")
    private String mimeType;

    @JsonProperty("thumbnail_url")
    private String thumbnailUrl;

    @JsonProperty("title")
    private String title;

    @JsonProperty("caption")
    private String caption;

    @JsonProperty("parse_mode")
    private String parseMode;

    @JsonProperty("caption_entities")
    private List<MessageEntity> captionEntities;

    @JsonProperty("video_width")
    private Integer videoWidth;

    @JsonProperty("video_height")
    private Integer videoHeight;

    @JsonProperty("video_duration")
    private Integer videoDuration;

    @JsonProperty("description")
    private String description;

    @JsonProperty("reply_markup")
    private InlineKeyboardMarkup replyMarkup;

    @JsonProperty("input_message_content")
    private InputMessageContent inputMessageContent;

    private InlineQueryResultVideo() {
    }
}


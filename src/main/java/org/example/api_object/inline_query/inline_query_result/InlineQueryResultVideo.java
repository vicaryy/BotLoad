package org.example.api_object.inline_query.inline_query_result;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.example.api_object.keyboard.InlineKeyboardMarkup;
import org.example.api_object.message.MessageEntity;

import java.util.List;

@Getter
public class InlineQueryResultVideo implements InlineQueryResult {
    /**
     * Represents a link to a page containing an embedded video player or a video file. By default, this video file will be sent by the user
     * with an optional caption. Alternatively, you can use input_message_content to send a message with the specified content instead of the video.
     */
    @JsonProperty("type")
    private final String type = "video";

    @JsonProperty("id")
    public String id;

    @JsonProperty("video_url")
    public String videoUrl;

    @JsonProperty("mime_type")
    public String mimeType;

    @JsonProperty("thumbnail_url")
    public String thumbnailUrl;

    @JsonProperty("title")
    public String title;

    @JsonProperty("caption")
    public String caption;

    @JsonProperty("parse_mode")
    public String parseMode;

    @JsonProperty("caption_entities")
    public List<MessageEntity> captionEntities;

    @JsonProperty("video_width")
    public Integer videoWidth;

    @JsonProperty("video_height")
    public Integer videoHeight;

    @JsonProperty("video_duration")
    public Integer videoDuration;

    @JsonProperty("description")
    public String description;

    @JsonProperty("reply_markup")
    public InlineKeyboardMarkup replyMarkup;

    @JsonProperty("input_message_content")
    public InputMessageContent inputMessageContent;

    private InlineQueryResultVideo() {
    }
}


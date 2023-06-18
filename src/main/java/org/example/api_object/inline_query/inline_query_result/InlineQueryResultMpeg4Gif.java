package org.example.api_object.inline_query.inline_query_result;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.example.api_object.keyboard.InlineKeyboardMarkup;
import org.example.api_object.message.MessageEntity;

import java.util.List;

@Getter
public class InlineQueryResultMpeg4Gif implements InlineQueryResult {
    /**
     * Represents a link to a video animation (H.264/MPEG-4 AVC video without sound). By default, this animated MPEG-4 file will be sent by the user
     * with optional caption. Alternatively, you can use input_message_content to send a message with the specified content instead of the animation.
     */
    @JsonProperty("type")
    public String type;

    @JsonProperty("id")
    public String id;

    @JsonProperty("mpeg4_url")
    public String mpeg4Url;

    @JsonProperty("mpeg4_width")
    public Integer mpeg4Width;

    @JsonProperty("mpeg4_height")
    public Integer mpeg4Height;

    @JsonProperty("mpeg4_duration")
    public Integer mpeg4Duration;

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

    private InlineQueryResultMpeg4Gif() {
    }
}

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
public class InlineQueryResultCachedVideo implements InlineQueryResult {
    /**
     * Represents a link to a video file stored on the Telegram servers.
     */
    @JsonProperty("type")
    private final String type = "video";

    @JsonProperty("id")
    private String id;

    @JsonProperty("video_file_id")
    private String videoFileId;

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

    private InlineQueryResultCachedVideo() {
    }
}

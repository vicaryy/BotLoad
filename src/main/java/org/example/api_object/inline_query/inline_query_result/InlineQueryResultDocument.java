package org.example.api_object.inline_query.inline_query_result;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.example.api_object.keyboard.InlineKeyboardMarkup;
import org.example.api_object.message.MessageEntity;

import java.util.List;

@Getter
public class InlineQueryResultDocument implements InlineQueryResult {
    /**
     * Represents a link to a file. By default, this file will be sent by the user with an optional caption.
     * Alternatively, you can use input_message_content to send a message with the specified content instead of the file.
     * Currently, only .PDF and .ZIP files can be sent using this method.
     */
    @JsonProperty("type")
    private final String type = "document";

    @JsonProperty("id")
    public String id;

    @JsonProperty("title")
    public String title;

    @JsonProperty("caption")
    public String caption;

    @JsonProperty("parse_mode")
    public String parseMode;

    @JsonProperty("caption_entities")
    public List<MessageEntity> captionEntities;

    @JsonProperty("document_url")
    public String documentUrl;

    @JsonProperty("mime_type")
    public String mimeType;

    @JsonProperty("description")
    public String description;

    @JsonProperty("reply_markup")
    public InlineKeyboardMarkup replyMarkup;

    @JsonProperty("input_message_content")
    public InputMessageContent inputMessageContent;

    @JsonProperty("thumbnail_url")
    public String thumbnailUrl;

    @JsonProperty("thumbnail_width")
    public Integer thumbnailWidth;

    @JsonProperty("thumbnail_height")
    public Integer thumbnailHeight;

    private InlineQueryResultDocument() {
    }
}

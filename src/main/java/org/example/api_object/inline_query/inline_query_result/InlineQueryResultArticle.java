package org.example.api_object.inline_query.inline_query_result;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.example.api_object.keyboard.InlineKeyboardMarkup;

@Getter
public class InlineQueryResultArticle implements InlineQueryResult {
    /**
     * Represents a link to an article or web page.
     */
    @JsonProperty("type")
    public String type;

    @JsonProperty("id")
    public String id;

    @JsonProperty("title")
    public String title;

    @JsonProperty("input_message_content")
    public InputMessageContent inputMessageContent;

    @JsonProperty("reply_markup")
    public InlineKeyboardMarkup replyMarkup;

    @JsonProperty("url")
    public String url;

    @JsonProperty("hide_url")
    public Boolean hideUrl;

    @JsonProperty("description")
    public String description;

    @JsonProperty("thumbnail_url")
    public String thumbnailUrl;

    @JsonProperty("thumbnail_width")
    public Integer thumbnailWidth;

    @JsonProperty("thumbnail_height")
    public Integer thumbnailHeight;

    private InlineQueryResultArticle() {
    }
}

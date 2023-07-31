package org.vicary.api_object.inline_query.inline_query_result;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.vicary.api_object.inline_query.input_message_content.InputMessageContent;
import org.vicary.api_object.keyboard.InlineKeyboardMarkup;

@Getter
@ToString
@EqualsAndHashCode
public class InlineQueryResultArticle implements InlineQueryResult {
    /**
     * Represents a link to an article or web page.
     */
    @JsonProperty("type")
    private String type;

    @JsonProperty("id")
    private String id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("input_message_content")
    private InputMessageContent inputMessageContent;

    @JsonProperty("reply_markup")
    private InlineKeyboardMarkup replyMarkup;

    @JsonProperty("url")
    private String url;

    @JsonProperty("hide_url")
    private Boolean hideUrl;

    @JsonProperty("description")
    private String description;

    @JsonProperty("thumbnail_url")
    private String thumbnailUrl;

    @JsonProperty("thumbnail_width")
    private Integer thumbnailWidth;

    @JsonProperty("thumbnail_height")
    private Integer thumbnailHeight;

    private InlineQueryResultArticle() {
    }
}

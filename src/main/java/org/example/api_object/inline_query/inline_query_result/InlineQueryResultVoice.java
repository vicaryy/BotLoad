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
public class InlineQueryResultVoice implements InlineQueryResult {
    /**
     * Represents a link to a voice recording in an .OGG container encoded with OPUS. By default, this voice recording
     * will be sent by the user. Alternatively, you can use input_message_content to send a message with the specified
     * content instead of the voice message.
     */
    @JsonProperty("type")
    private final String type = "voice";

    @JsonProperty("id")
    private String id;

    @JsonProperty("voice_url")
    private String voiceUrl;

    @JsonProperty("title")
    private String title;

    @JsonProperty("caption")
    private String caption;

    @JsonProperty("parse_mode")
    private String parseMode;

    @JsonProperty("caption_entities")
    private List<MessageEntity> captionEntities;

    @JsonProperty("voice_duration")
    private Integer voiceDuration;

    @JsonProperty("reply_markup")
    private InlineKeyboardMarkup replyMarkup;

    @JsonProperty("input_message_content")
    private InputMessageContent inputMessageContent;

    private InlineQueryResultVoice() {
    }
}

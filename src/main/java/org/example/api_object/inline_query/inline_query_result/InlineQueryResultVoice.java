package org.example.api_object.inline_query.inline_query_result;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.example.api_object.keyboard.InlineKeyboardMarkup;
import org.example.api_object.message.MessageEntity;

import java.util.List;

@Getter
public class InlineQueryResultVoice implements InlineQueryResult {
    /**
     * Represents a link to a voice recording in an .OGG container encoded with OPUS. By default, this voice recording
     * will be sent by the user. Alternatively, you can use input_message_content to send a message with the specified
     * content instead of the voice message.
     */
    @JsonProperty("type")
    private final String type = "voice";

    @JsonProperty("id")
    public String id;

    @JsonProperty("voice_url")
    public String voiceUrl;

    @JsonProperty("title")
    public String title;

    @JsonProperty("caption")
    public String caption;

    @JsonProperty("parse_mode")
    public String parseMode;

    @JsonProperty("caption_entities")
    public List<MessageEntity> captionEntities;

    @JsonProperty("voice_duration")
    public Integer voiceDuration;

    @JsonProperty("reply_markup")
    public InlineKeyboardMarkup replyMarkup;

    @JsonProperty("input_message_content")
    public InputMessageContent inputMessageContent;

    private InlineQueryResultVoice() {
    }
}

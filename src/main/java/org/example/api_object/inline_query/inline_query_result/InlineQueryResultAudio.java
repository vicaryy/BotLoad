package org.example.api_object.inline_query.inline_query_result;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.example.api_object.keyboard.InlineKeyboardMarkup;
import org.example.api_object.message.MessageEntity;

import java.util.List;

@Getter
public class InlineQueryResultAudio implements InlineQueryResult {
    /**
     * Represents a link to an MP3 audio file. By default, this audio file will be sent by the user. Alternatively,
     * you can use input_message_content to send a message with the specified content instead of the audio.
     */
    @JsonProperty("type")
    private final String type = "audio";

    @JsonProperty("id")
    public String id;

    @JsonProperty("audio_url")
    public String audioUrl;

    @JsonProperty("title")
    public String title;

    @JsonProperty("caption")
    public String caption;

    @JsonProperty("parse_mode")
    public String parseMode;

    @JsonProperty("caption_entities")
    public List<MessageEntity> captionEntities;

    @JsonProperty("performer")
    public String performer;

    @JsonProperty("audio_duration")
    public Integer audioDuration;

    @JsonProperty("reply_markup")
    public InlineKeyboardMarkup replyMarkup;

    @JsonProperty("input_message_content")
    public InputMessageContent inputMessageContent;

    private InlineQueryResultAudio() {
    }
}
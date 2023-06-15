package org.example.api_response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message implements ApiResponseObject {
    @JsonProperty("message_id")
    private Integer messageId;
    @JsonProperty("message_thread_id")
    private Integer messageThreadId;
    @JsonProperty("from")
    private User from;
    @JsonProperty("sender_chat")
    private Chat senderChat;
    @JsonProperty("date")
    private Integer date;
    @JsonProperty("chat")
    private Chat chat;
    @JsonProperty("text")
    private String text;
}

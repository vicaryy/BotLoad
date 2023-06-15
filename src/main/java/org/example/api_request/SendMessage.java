package org.example.api_request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SendMessage {
    @JsonProperty("chat_id")
    private Integer chatId;
    @JsonProperty("message_thread_id")
    private Integer messageThreadId;
    @JsonProperty("text")
    private String text;
}

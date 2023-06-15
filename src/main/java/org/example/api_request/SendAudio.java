package org.example.api_request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SendAudio {
    @JsonProperty("chat_id")
    private Integer chatId;
    @JsonProperty("audio")
    private InputFile audio;
}

package org.example.api_request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SendAnimation {
    @JsonProperty("chat_id")
    private String chatId;
    @JsonProperty("animation")
    private InputFile animation;
}

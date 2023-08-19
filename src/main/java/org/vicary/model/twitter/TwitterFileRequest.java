package org.vicary.model.twitter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.vicary.api_request.edit_message.EditMessageText;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TwitterFileRequest {
    private String url;

    private String chatId;

    private Boolean premium;

    private final String extension = "mp4";

    private final Integer multiVideoNumber = 2;

    private EditMessageText editMessageText;
}

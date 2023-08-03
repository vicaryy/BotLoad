package org.vicary.model;

import lombok.*;
import org.vicary.api_request.InputFile;
import org.vicary.api_request.edit_message.EditMessageText;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class YouTubeFileRequest {
    private String youtubeId;

    private String chatId;

    private String extension;

    private Boolean premium;

    private EditMessageText editMessageText;
}

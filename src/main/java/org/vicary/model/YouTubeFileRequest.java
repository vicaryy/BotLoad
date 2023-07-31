package org.vicary.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class YouTubeFileRequest {
    private String youtubeId;

    private String chatId;

    private String extension;

    private Boolean premium;
}

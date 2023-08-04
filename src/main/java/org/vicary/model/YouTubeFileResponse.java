package org.vicary.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.vicary.api_request.InputFile;
import org.vicary.api_request.edit_message.EditMessageText;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class YouTubeFileResponse {
    private String youtubeId;

    private String extension;

    private Boolean premium;

    private String title;

    private Integer duration;

    private Long size;

    private String artist;

    private String track;

    private String album;

    private String releaseYear;

    private InputFile downloadedFile;

    private InputFile thumbnail;

    private EditMessageText editMessageText;
}

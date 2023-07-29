package org.example.service.youtube;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.api_request.InputFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class YouTubeFile {
    private String fileId;

    private String youtubeId;

    private InputFile file;

    private InputFile thumbnail;

    private String extension;

    private String size;

    private String duration;
}

package org.example.service.mapper;

import org.example.entity.YoutubeFileEntity;
import org.example.service.youtube.YouTubeFile;
import org.springframework.stereotype.Component;

@Component
public class YouTubeFileMapper {

    public YoutubeFileEntity map(YouTubeFile youTubeFile) {
        return YoutubeFileEntity.builder()
                .youtubeId(youTubeFile.getYoutubeId())
                .extension(youTubeFile.getExtension())
                .size(youTubeFile.getSize())
                .duration(youTubeFile.getDuration())
                .fileId(youTubeFile.getFileId())
                .build();
    }
}

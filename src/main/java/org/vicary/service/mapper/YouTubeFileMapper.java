package org.vicary.service.mapper;

import org.springframework.stereotype.Component;
import org.vicary.model.youtube.YouTubeFileInfo;
import org.vicary.model.youtube.YouTubeFileResponse;

@Component
public class YouTubeFileMapper {

    public YouTubeFileResponse map(YouTubeFileInfo youTubeFileInfo) {
        return YouTubeFileResponse.builder()
                .youtubeId(youTubeFileInfo.getYoutubeId())
                .title(youTubeFileInfo.getTitle())
                .duration(youTubeFileInfo.getDuration())
                .artist(youTubeFileInfo.getArtist())
                .track(youTubeFileInfo.getTrack())
                .album(youTubeFileInfo.getAlbum())
                .releaseYear(youTubeFileInfo.getReleaseYear())
                .build();
    }
}

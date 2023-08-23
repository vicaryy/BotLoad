package org.vicary.service.mapper;

import org.springframework.stereotype.Component;
import org.vicary.model.FileInfo;
import org.vicary.model.FileResponse;

@Component
public class FileInfoMapper {

    public FileResponse map(FileInfo fileInfo) {
        return FileResponse.builder()
                .id(fileInfo.getId())
                .title(fileInfo.getTitle())
                .duration(fileInfo.getDuration())
                .artist(fileInfo.getArtist())
                .track(fileInfo.getTrack())
                .album(fileInfo.getAlbum())
                .releaseYear(fileInfo.getReleaseYear())
                .URL(fileInfo.getURL())
                .build();
    }
}

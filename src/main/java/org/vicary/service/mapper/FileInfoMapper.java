package org.vicary.service.mapper;

import org.springframework.stereotype.Component;
import org.vicary.model.FileInfo;
import org.vicary.model.FileResponse;

@Component
public class FileInfoMapper {

    public FileResponse map(FileInfo fileInfo) {
        String title = fileInfo.getTitle() == null ? "title" : fileInfo.getTitle().isEmpty() ? "title" : fileInfo.getTitle();
        return FileResponse.builder()
                .id(fileInfo.getId())
                .title(title)
                .duration(fileInfo.getDuration())
                .artist(fileInfo.getArtist())
                .track(fileInfo.getTrack())
                .album(fileInfo.getAlbum())
                .releaseYear(fileInfo.getReleaseYear())
                .URL(fileInfo.getURL())
                .build();
    }
}

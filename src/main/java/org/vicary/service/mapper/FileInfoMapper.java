package org.vicary.service.mapper;

import org.springframework.stereotype.Component;
import org.vicary.model.FileInfo;
import org.vicary.model.FileResponse;
import org.vicary.model.ID3TagData;

@Component
public class FileInfoMapper {

    public FileResponse map(FileInfo fileInfo, String serviceName) {
        String title = fileInfo.getTitle() == null || fileInfo.getTitle().isBlank() ? "title" : fileInfo.getTitle();
        ID3TagData id3TagData = null;
        if (fileInfo.getArtist() != null && serviceName.equals("youtube")) {
            id3TagData = ID3TagData.builder()
                    .artist(fileInfo.getArtist())
                    .title(fileInfo.getTrack())
                    .album(fileInfo.getAlbum())
                    .releaseYear(fileInfo.getReleaseYear())
                    .build();
        }
        return FileResponse.builder()
                .serviceId(fileInfo.getId())
                .title(title)
                .duration(fileInfo.getDuration())
                .id3TagData(id3TagData)
                .URL(fileInfo.getURL())
                .build();
    }
}

package org.vicary.service;

import com.mpatric.mp3agic.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.vicary.model.FileResponse;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ID3TagService {

    private final static Logger logger = LoggerFactory.getLogger(ID3TagService.class);

    private final TerminalExecutor terminalExecutor;

    public FileResponse addID3Tag(FileResponse fileResponse) {
        try {
            Mp3File mp3File = new Mp3File(fileResponse.getDownloadedFile().getFile());
            ID3v2 id3v2Tag;
            if (mp3File.hasId3v2Tag()) {
                id3v2Tag = mp3File.getId3v2Tag();
            } else {
                id3v2Tag = new ID3v24Tag();
                mp3File.setId3v2Tag(id3v2Tag);
            }

            id3v2Tag.setTitle(fileResponse.getTrack());
            id3v2Tag.setArtist(fileResponse.getArtist());
            id3v2Tag.setAlbum(fileResponse.getAlbum());
            id3v2Tag.setYear(fileResponse.getReleaseYear());

        } catch (IOException | InvalidDataException | UnsupportedTagException e) {
            logger.warn("Failed to add ID3Tag to file id '{}'", fileResponse.getId());
        }
        return fileResponse;
    }
}

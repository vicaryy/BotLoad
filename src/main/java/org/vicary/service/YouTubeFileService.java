package org.vicary.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.vicary.entity.YouTubeFileEntity;
import org.vicary.repository.YoutubeFileRepository;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class YouTubeFileService {
    private final YoutubeFileRepository repository;

    public void saveYouTubeFile(YouTubeFileEntity youTubeFileEntity) {
        if (youTubeFileEntity != null)
            repository.save(youTubeFileEntity);
        else {
            throw new NoSuchElementException("YoutubeFileEntity cannot be null!");
        }
    }

    public YouTubeFileEntity findByYoutubeIdAndExtensionAndQuality(String youtubeId, String extension, String quality) {
        YouTubeFileEntity youTubeFileEntity = repository.findByYoutubeIdAndExtensionAndQuality(youtubeId, extension, quality);
        return youTubeFileEntity;
    }

    public boolean existsByYoutubeIdAndExtensionAndQuality(String youtubeId, String extension, String quality) {
        return repository.existsByYoutubeIdAndExtensionAndQuality(youtubeId, extension, quality);
    }
}

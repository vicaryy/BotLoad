package org.vicary.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.vicary.entity.YouTubeFileEntity;
import org.vicary.repository.YoutubeFileRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class YouTubeFileService {
    private final YoutubeFileRepository repository;

    public YouTubeFileEntity saveYouTubeFile(YouTubeFileEntity youTubeFileEntity) {
        return repository.save(youTubeFileEntity);
    }

    public Optional<YouTubeFileEntity> findByYoutubeIdAndExtensionAndQuality(String youtubeId, String extension, String quality) {
        return repository.findByYoutubeIdAndExtensionAndQuality(youtubeId, extension, quality);
    }

    public boolean existsByYoutubeIdAndExtensionAndQuality(String youtubeId, String extension, String quality) {
        return repository.existsByYoutubeIdAndExtensionAndQuality(youtubeId, extension, quality);
    }
}

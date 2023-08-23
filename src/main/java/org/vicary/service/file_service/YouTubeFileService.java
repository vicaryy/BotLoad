package org.vicary.service.file_service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.vicary.entity.YouTubeFileEntity;
import org.vicary.model.FileResponse;
import org.vicary.repository.YoutubeFileRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class YouTubeFileService {
    private static final Logger logger = LoggerFactory.getLogger(YouTubeFileService.class);
    private final YoutubeFileRepository repository;

    public YouTubeFileEntity saveYouTubeFile(YouTubeFileEntity youTubeFileEntity) {
        logger.info("New YouTube file '{}' saved to repository.", youTubeFileEntity.getFileId());
        return repository.save(youTubeFileEntity);
    }

    public Optional<YouTubeFileEntity> findByYoutubeIdAndExtensionAndQuality(String youtubeId, String extension, String quality) {
        return repository.findByYoutubeIdAndExtensionAndQuality(youtubeId, extension, quality);
    }

    public boolean existsByYoutubeIdAndExtensionAndQuality(String youtubeId, String extension, String quality) {
        return repository.existsByYoutubeIdAndExtensionAndQuality(youtubeId, extension, quality);
    }

    public boolean existsInRepo(FileResponse response) {
        String youtubeId = response.getId();
        String extension = response.getExtension();
        String quality = response.isPremium() ? "premium" : "standard";
        return existsByYoutubeIdAndExtensionAndQuality(youtubeId, extension, quality);
    }
}

package org.vicary.service.file_service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.vicary.entity.YouTubeFileEntity;
import org.vicary.model.FileResponse;
import org.vicary.repository.YoutubeFileRepository;
import org.vicary.service.Converter;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class YouTubeFileService implements FileService{
    private static final Logger logger = LoggerFactory.getLogger(YouTubeFileService.class);

    private final YoutubeFileRepository repository;

    private final Converter converter;

    public YouTubeFileEntity save(YouTubeFileEntity youTubeFileEntity) {
        logger.info("New YouTube file '{}' saved to repository.", youTubeFileEntity.getFileId());
        return repository.save(youTubeFileEntity);
    }

    public Optional<YouTubeFileEntity> findByYoutubeIdAndExtensionAndQuality(String youtubeId, String extension, String quality) {
        return repository.findByYoutubeIdAndExtensionAndQuality(youtubeId, extension, quality);
    }

    public boolean existsByYoutubeIdAndExtensionAndQuality(String youtubeId, String extension, String quality) {
        return repository.existsByYoutubeIdAndExtensionAndQuality(youtubeId, extension, quality);
    }

    @Override
    public void saveInRepo(FileResponse response) {
        save(YouTubeFileEntity.builder()
                .youtubeId(response.getId())
                .extension(response.getExtension())
                .quality(response.isPremium() ? "premium" : "standard")
                .size(converter.bytesToMB(response.getSize()))
                .duration(converter.secondsToMinutes(response.getDuration()))
                .title(response.getTitle())
                .URL(response.getURL())
                .fileId(response.getTelegramFileId())
                .build());
    }

    public boolean existsInRepo(FileResponse response) {
        String youtubeId = response.getId();
        String extension = response.getExtension();
        String quality = response.isPremium() ? "premium" : "standard";
        return existsByYoutubeIdAndExtensionAndQuality(youtubeId, extension, quality);
    }
}

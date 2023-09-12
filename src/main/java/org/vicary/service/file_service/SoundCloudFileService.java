package org.vicary.service.file_service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.vicary.entity.SoundCloudFileEntity;
import org.vicary.model.FileResponse;
import org.vicary.repository.SoundCloudFileRepository;
import org.vicary.service.Converter;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SoundCloudFileService implements FileService {
    private static final Logger logger = LoggerFactory.getLogger(YouTubeFileService.class);

    private final SoundCloudFileRepository repository;

    private final Converter converter;

    public SoundCloudFileEntity save(SoundCloudFileEntity soundCloudFileEntity) {
        logger.info("New SoundCloud file '{}' saved to repository.", soundCloudFileEntity.getFileId());
        return repository.save(soundCloudFileEntity);
    }

    public Optional<SoundCloudFileEntity> findBySoundcloudIdAndExtensionAndQuality(String soundcloudId, String extension, String quality) {
        return repository.findBySoundcloudIdAndExtensionAndQuality(soundcloudId, extension, quality);
    }

    public boolean existsBySoundcloudIdAndExtensionAndQuality(String id, String extension, String quality) {
        return repository.existsBySoundcloudIdAndExtensionAndQuality(id, extension, quality);
    }

    @Override
    public void saveInRepo(FileResponse response) {
        save(SoundCloudFileEntity.builder()
                .soundcloudId(response.getServiceId())
                .extension(response.getExtension())
                .quality(response.isPremium() ? "premium" : "standard")
                .size(converter.bytesToMB(response.getSize()))
                .duration(converter.secondsToMinutes(response.getDuration()))
                .title(response.getTitle())
                .URL(response.getURL())
                .fileId(response.getTelegramFileId())
                .build());
    }

    @Override
    public boolean existsInRepo(FileResponse response) {
        String id = response.getServiceId();
        String extension = response.getExtension();
        String quality = response.isPremium() ? "premium" : "standard";
        return existsBySoundcloudIdAndExtensionAndQuality(id, extension, quality);
    }
}

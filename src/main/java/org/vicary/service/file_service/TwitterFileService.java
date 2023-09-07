package org.vicary.service.file_service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.vicary.entity.TwitterFileEntity;
import org.vicary.entity.YouTubeFileEntity;
import org.vicary.model.FileResponse;
import org.vicary.repository.TwitterFileRepository;
import org.vicary.service.Converter;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TwitterFileService implements FileService{
    private final TwitterFileRepository repository;

    private final Converter converter;

    public TwitterFileEntity saveEntity(TwitterFileEntity twitterFileEntity) {
        return repository.save(twitterFileEntity);
    }

    public Optional<TwitterFileEntity> findByTwitterId(String twitterId) {
        return repository.findByTwitterId(twitterId);
    }

    public Optional<TwitterFileEntity> findByTwitterIdAndExtensionAndQuality(String youtubeId, String extension, String quality) {
        return repository.findByTwitterIdAndExtensionAndQuality(youtubeId, extension, quality);
    }

    public boolean existsByTwitterIdAndExtensionAndQuality(String id, String extension, String quality) {
        return repository.existsByTwitterIdAndExtensionAndQuality(id, extension, quality);
    }
    @Override
    public void saveInRepo(FileResponse response) {
        saveEntity(TwitterFileEntity.builder()
                .twitterId(response.getId())
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
        String id = response.getId();
        String extension = response.getExtension();
        String quality = response.isPremium() ? "premium" : "standard";
        return existsByTwitterIdAndExtensionAndQuality(id, extension, quality);
    }
}

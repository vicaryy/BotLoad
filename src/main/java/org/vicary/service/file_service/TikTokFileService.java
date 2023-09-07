package org.vicary.service.file_service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.vicary.entity.TikTokFileEntity;
import org.vicary.entity.YouTubeFileEntity;
import org.vicary.model.FileResponse;
import org.vicary.repository.TikTokFileRepository;
import org.vicary.service.Converter;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TikTokFileService implements FileService {

    private final static Logger logger = LoggerFactory.getLogger(TikTokFileService.class);

    private final TikTokFileRepository repository;

    private final Converter converter;

    public TikTokFileEntity saveEntity(TikTokFileEntity tikTokFileEntity) {
        return repository.save(tikTokFileEntity);
    }

    public Optional<TikTokFileEntity> findByTikTokId(String tiktokId) {
        return repository.findByTiktokId(tiktokId);
    }

    public Optional<TikTokFileEntity> findByTiktokIdAndExtensionAndQuality(String youtubeId, String extension, String quality) {
        return repository.findByTiktokIdAndExtensionAndQuality(youtubeId, extension, quality);
    }

    public boolean existsByTiktokIdAndExtensionAndQuality(String id, String extension, String quality) {
        return repository.existsByTiktokIdAndExtensionAndQuality(id, extension, quality);
    }
    @Override
    public void saveInRepo(FileResponse response) {
        saveEntity(TikTokFileEntity.builder()
                .tiktokId(response.getId())
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
        return existsByTiktokIdAndExtensionAndQuality(id, extension, quality);
    }
}

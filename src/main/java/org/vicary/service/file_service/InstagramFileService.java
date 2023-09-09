package org.vicary.service.file_service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.vicary.entity.InstagramFileEntity;
import org.vicary.model.FileResponse;
import org.vicary.repository.InstagramFileRepository;
import org.vicary.service.Converter;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InstagramFileService implements FileService {
    private final InstagramFileRepository repository;

    private final Converter converter;

    public InstagramFileEntity saveEntity(InstagramFileEntity instagramFileEntity) {
        return repository.save(instagramFileEntity);
    }

    public Optional<InstagramFileEntity> findByInstagramId(String instagramId) {
        return repository.findByInstagramId(instagramId);
    }

    public Optional<InstagramFileEntity> findByInstagramIdAndExtensionAndQuality(String youtubeId, String extension, String quality) {
        return repository.findByInstagramIdAndExtensionAndQuality(youtubeId, extension, quality);
    }

    public boolean existsByInstagramIdAndExtensionAndQuality(String id, String extension, String quality) {
        return repository.existsByInstagramIdAndExtensionAndQuality(id, extension, quality);
    }
    @Override
    public void saveInRepo(FileResponse response) {
        saveEntity(InstagramFileEntity.builder()
                .instagramId(response.getServiceId())
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
        return existsByInstagramIdAndExtensionAndQuality(id, extension, quality);
    }
}

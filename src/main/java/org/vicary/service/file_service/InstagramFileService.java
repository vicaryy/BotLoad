package org.vicary.service.file_service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.vicary.entity.InstagramFileEntity;
import org.vicary.entity.TwitterFileEntity;
import org.vicary.model.FileResponse;
import org.vicary.repository.InstagramFileRepository;
import org.vicary.repository.TwitterFileRepository;
import org.vicary.service.Converter;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InstagramFileService implements FileService {
    private final InstagramFileRepository repository;

    public InstagramFileEntity saveEntity(InstagramFileEntity instagramFileEntity) {
        return repository.save(instagramFileEntity);
    }

    public Optional<InstagramFileEntity> findByInstagramId(String instagramId) {
        return repository.findByInstagramId(instagramId);
    }

    public boolean existsByTwitterId(String instagramId) {
        return repository.existsByInstagramId(instagramId);
    }

    @Override
    public void saveInRepo(FileResponse response) {
        saveEntity(InstagramFileEntity.builder()
                .instagramId(response.getId())
                .extension(response.getExtension())
                .quality(response.isPremium() ? "premium" : "standard")
                .size(Converter.bytesToMB(response.getSize()))
                .duration(Converter.secondsToMinutes(response.getDuration()))
                .title(response.getTitle())
                .URL(response.getURL())
                .fileId(response.getTelegramFileId())
                .build());
    }

    @Override
    public boolean existsInRepo(FileResponse response) {
        return existsByTwitterId(response.getId());
    }
}

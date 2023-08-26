package org.vicary.service.file_service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.vicary.entity.TikTokFileEntity;
import org.vicary.model.FileResponse;
import org.vicary.repository.TikTokFileRepository;
import org.vicary.service.Converter;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TikTokFileService implements FileService {

    private final TikTokFileRepository repository;

    public TikTokFileEntity saveEntity(TikTokFileEntity tikTokFileEntity) {
        return repository.save(tikTokFileEntity);
    }

    public Optional<TikTokFileEntity> findByTwitterId(String tiktokId) {
        return repository.findByTiktokId(tiktokId);
    }

    public boolean existsByTikTokId(String tiktokId) {
        return repository.existsByTiktokId(tiktokId);
    }

    @Override
    public void saveInRepo(FileResponse response) {
        saveEntity(TikTokFileEntity.builder()
                .tiktokId(response.getId())
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
        return existsByTikTokId(response.getId());
    }
}

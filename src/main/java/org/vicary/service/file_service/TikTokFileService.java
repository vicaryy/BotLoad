package org.vicary.service.file_service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.vicary.entity.TikTokFileEntity;
import org.vicary.repository.TikTokFileRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TikTokFileService {

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
}

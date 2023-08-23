package org.vicary.service.file_service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.vicary.entity.InstagramFileEntity;
import org.vicary.entity.TwitterFileEntity;
import org.vicary.repository.InstagramFileRepository;
import org.vicary.repository.TwitterFileRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InstagramFileService {
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
}

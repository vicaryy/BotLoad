package org.vicary.service.file_service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.vicary.entity.TwitterFileEntity;
import org.vicary.repository.TwitterFileRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TwitterFileService {
    private final TwitterFileRepository repository;

    public TwitterFileEntity saveEntity(TwitterFileEntity twitterFileEntity) {
        return repository.save(twitterFileEntity);
    }

    public Optional<TwitterFileEntity> findByTwitterId(String twitterId) {
        return repository.findByTwitterId(twitterId);
    }

    public boolean existsByTwitterId(String twitterId) {
        return repository.existsByTwitterId(twitterId);
    }
}

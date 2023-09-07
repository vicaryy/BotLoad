package org.vicary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vicary.entity.InstagramFileEntity;
import org.vicary.entity.YouTubeFileEntity;

import java.util.Optional;

public interface InstagramFileRepository extends JpaRepository<InstagramFileEntity, Long> {
    Optional<InstagramFileEntity> findByInstagramId(String instagramId);

    Optional<InstagramFileEntity> findByInstagramIdAndExtensionAndQuality(String instagramId, String extension, String quality);

    boolean existsByInstagramIdAndExtensionAndQuality(String instagramId, String extension, String quality);
}

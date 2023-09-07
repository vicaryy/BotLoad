package org.vicary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vicary.entity.TikTokFileEntity;
import org.vicary.entity.TwitterFileEntity;
import org.vicary.entity.YouTubeFileEntity;

import java.util.Optional;

public interface TikTokFileRepository extends JpaRepository<TikTokFileEntity, Long> {
    Optional<TikTokFileEntity> findByTiktokId(String tiktokId);

    Optional<TikTokFileEntity> findByTiktokIdAndExtensionAndQuality(String tiktokId, String extension, String quality);

    boolean existsByTiktokIdAndExtensionAndQuality(String tiktokId, String extension, String quality);

}

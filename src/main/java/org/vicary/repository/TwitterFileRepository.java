package org.vicary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vicary.entity.TwitterFileEntity;
import org.vicary.entity.YouTubeFileEntity;

import java.util.Optional;

public interface TwitterFileRepository extends JpaRepository<TwitterFileEntity, Long> {
    Optional<TwitterFileEntity> findByTwitterId(String twitterId);

    Optional<TwitterFileEntity> findByTwitterIdAndExtensionAndQuality(String twitterId, String extension, String quality);

    boolean existsByTwitterIdAndExtensionAndQuality(String twitterId, String extension, String quality);

}

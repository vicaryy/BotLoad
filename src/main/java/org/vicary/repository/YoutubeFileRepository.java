package org.vicary.repository;

import org.vicary.entity.YouTubeFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface YoutubeFileRepository extends JpaRepository<YouTubeFileEntity, Long> {
    Optional<YouTubeFileEntity> findByYoutubeIdAndExtensionAndQuality(String youtubeId, String extension, String quality);
    boolean existsByYoutubeIdAndExtensionAndQuality(String youtubeId, String extension, String quality);
}

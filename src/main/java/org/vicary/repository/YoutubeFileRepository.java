package org.vicary.repository;

import org.vicary.entity.YouTubeFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface YoutubeFileRepository extends JpaRepository<YouTubeFileEntity, Long> {
    YouTubeFileEntity findByYoutubeIdAndExtensionAndQuality(String youtubeId, String extension, String quality);
    boolean existsByYoutubeIdAndExtensionAndQuality(String youtubeId, String extension, String quality);
}

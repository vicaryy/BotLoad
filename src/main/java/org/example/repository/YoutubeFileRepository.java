package org.example.repository;

import org.example.entity.YouTubeFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface YoutubeFileRepository extends JpaRepository<YouTubeFileEntity, Long> {
    YouTubeFileEntity findByYoutubeIdAndExtensionAndQuality(String youtubeId, String extension, String quality);
}

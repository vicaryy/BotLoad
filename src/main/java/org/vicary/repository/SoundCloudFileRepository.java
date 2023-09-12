package org.vicary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vicary.entity.SoundCloudFileEntity;

import java.util.Optional;

public interface SoundCloudFileRepository extends JpaRepository<SoundCloudFileEntity, Long> {
    Optional<SoundCloudFileEntity> findBySoundcloudIdAndExtensionAndQuality(String soundcloudId, String extension, String quality);
    boolean existsBySoundcloudIdAndExtensionAndQuality(String soundcloudId, String extension, String quality);
}

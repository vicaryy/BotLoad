package org.vicary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vicary.entity.TikTokFileEntity;

import java.util.Optional;

public interface TikTokFileRepository extends JpaRepository<TikTokFileEntity, Long> {
    Optional<TikTokFileEntity> findByTiktokId(String tiktokId);
    boolean existsByTiktokId(String tiktokId);
}

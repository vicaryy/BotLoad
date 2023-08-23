package org.vicary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vicary.entity.InstagramFileEntity;

import java.util.Optional;

public interface InstagramFileRepository extends JpaRepository<InstagramFileEntity, Long> {
    Optional<InstagramFileEntity> findByInstagramId(String instagramId);
    boolean existsByInstagramId(String instagramId);
}

package org.vicary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vicary.entity.TwitterFileEntity;

import java.util.Optional;

public interface TwitterFileRepository extends JpaRepository<TwitterFileEntity, Long> {
    Optional<TwitterFileEntity> findByTwitterId(String twitterId);
    boolean existsByTwitterId(String twitterId);
}

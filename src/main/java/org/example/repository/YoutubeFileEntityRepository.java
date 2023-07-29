package org.example.repository;

import org.example.entity.YoutubeFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface YoutubeFileEntityRepository extends JpaRepository<YoutubeFileEntity, String> {
}

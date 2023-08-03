package org.vicary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vicary.entity.ActiveRequestEntity;

public interface ActiveRequestRepository extends JpaRepository<ActiveRequestEntity, Long> {
    boolean existsByUserId(String userId);
    void deleteByUserId(String userId);
}

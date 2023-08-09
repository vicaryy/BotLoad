package org.vicary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.vicary.entity.ActiveRequestEntity;

public interface ActiveRequestRepository extends JpaRepository<ActiveRequestEntity, Long> {
    @Query(countQuery = "COUNT(1) FROM ACTIVE_REQUESTS WHERE USER_ID = :userId")
    boolean existsByUserId(String userId);

    @Query(name = "DELETE FROM ACTIVE_REQUESTS WHERE USER_ID = :userId")
    void deleteByUserId(String userId);
}

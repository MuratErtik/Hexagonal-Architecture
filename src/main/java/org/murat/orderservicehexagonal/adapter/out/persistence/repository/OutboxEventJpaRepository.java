package org.murat.orderservicehexagonal.adapter.out.persistence.repository;

import org.murat.orderservicehexagonal.adapter.out.persistence.entity.OutboxEventJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface OutboxEventJpaRepository extends JpaRepository<OutboxEventJpaEntity, UUID> {

    List<OutboxEventJpaEntity> findTop50ByStatusOrderByCreatedAtAsc(String status);

    @Modifying
    @Query("UPDATE OutboxEventJpaEntity o SET o.status = 'FAILED' WHERE o.retryCount >= :maxRetry AND o.status = 'PENDING'")
    int markDeadEvents(@Param("maxRetry") int maxRetry);
}

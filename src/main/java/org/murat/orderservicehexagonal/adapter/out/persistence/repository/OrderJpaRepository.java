package org.murat.orderservicehexagonal.adapter.out.persistence.repository;

import jakarta.persistence.LockModeType;
import org.murat.orderservicehexagonal.adapter.out.persistence.entity.OrderJpaEntity;
import org.murat.orderservicehexagonal.domain.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface OrderJpaRepository extends JpaRepository<OrderJpaEntity, UUID> {

    Page<OrderJpaEntity> findAllByUserId(UUID userId, Pageable pageable);

    boolean existsByUserIdAndStatus(UUID userId, OrderStatus status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT o FROM OrderJpaEntity o LEFT JOIN FETCH o.items WHERE o.id = :id")
    Optional<OrderJpaEntity> findByIdWithLock(@Param("id") UUID id);

    @Query("""                              
          SELECT DISTINCT o FROM OrderJpaEntity o
          JOIN o.items i
          WHERE i.sellerId = :sellerId                                                                                                                                                                   
          ORDER BY o.createdAt DESC                                                                                                                                                                    
          """)
    Page<OrderJpaEntity> findOrdersBySellerIdInItems(
            @Param("sellerId") UUID sellerId,
            Pageable pageable);
}

package org.murat.orderservicehexagonal.application.port.out;

import org.murat.orderservicehexagonal.domain.enums.OrderStatus;
import org.murat.orderservicehexagonal.domain.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepositoryPort {

    Order save(Order order);

    Optional<Order> findById(UUID id);

    boolean findByIdAndStatus(UUID id, OrderStatus status);

    Optional<Order> findByIdWithLock(UUID id);

    boolean existsByUserIdAndStatus(UUID userId, OrderStatus status);

    // it is though to separate domain and pagination lib which come from Spring, that's why we import it here!
    Page<Order> findAllByUserId(UUID userId, Pageable pageable);

    Page<Order> findOrdersBySellerIdInItems(UUID sellerId, Pageable pageable);

}





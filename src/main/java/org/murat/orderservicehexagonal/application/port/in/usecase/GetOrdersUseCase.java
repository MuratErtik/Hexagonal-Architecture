package org.murat.orderservicehexagonal.application.port.in.usecase;

import org.murat.orderservicehexagonal.domain.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface GetOrdersUseCase {

    Page<Order> getOrders(UUID userId, Pageable pageable);
}

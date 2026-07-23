package org.murat.orderservicehexagonal.application.service;

import lombok.RequiredArgsConstructor;

import org.murat.orderservicehexagonal.application.port.in.usecase.*;
import org.murat.orderservicehexagonal.application.port.out.OrderRepositoryPort;
import org.murat.orderservicehexagonal.domain.exception.OrderNotFoundException;
import org.murat.orderservicehexagonal.domain.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetOrderService implements GetOrderUseCase, GetOrdersUseCase, GetOrdersBySellerUseCase {

    private final OrderRepositoryPort orderRepositoryPort;

    @Override
    @Transactional(readOnly = true)
    public Order getOrder(UUID orderId, UUID userId) {
        Order order = orderRepositoryPort.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found. orderId: " + orderId));

        if (!order.getUserId().equals(userId)) {
            throw new OrderNotFoundException("Order not found. orderId: " + orderId);
        }

        return order;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Order> getOrders(UUID userId, Pageable pageable) {
        return orderRepositoryPort.findAllByUserId(userId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Order> getOrdersBySeller(UUID sellerId, Pageable pageable) {
        return orderRepositoryPort.findOrdersBySellerIdInItems(sellerId, pageable);
    }
}



package org.murat.orderservicehexagonal.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.murat.orderservicehexagonal.application.port.in.*;
import org.murat.orderservicehexagonal.application.port.in.usecase.*;
import org.murat.orderservicehexagonal.application.port.out.OrderRepositoryPort;
import org.murat.orderservicehexagonal.application.port.out.OutboxEventPort;
import org.murat.orderservicehexagonal.domain.enums.OrderStatus;
import org.murat.orderservicehexagonal.domain.event.OutboxMessage;
import org.murat.orderservicehexagonal.domain.exception.OrderNotFoundException;
import org.murat.orderservicehexagonal.domain.model.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderEventHandlerService implements
        HandleStockReservedUseCase,
        HandleStockReservationFailedUseCase,
        HandlePaymentCompletedUseCase,
        HandlePaymentFailedUseCase {

    private final OrderRepositoryPort orderRepositoryPort;
    private final OutboxEventPort outboxEventPort;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public void handleStockReserved(UUID orderId) {
        Order order = findWithLock(orderId);

        if (order.getStatus() != OrderStatus.PENDING) {
            log.warn("StockReservedEvent ignored. orderId={}, status={}", orderId, order.getStatus());
            return;
        }

        order.setStatus(OrderStatus.STOCK_RESERVED);

        outboxEventPort.save(OutboxMessage.builder()
                .aggregateId(orderId.toString())
                .aggregateType("ORDER")
                .eventType("PAYMENT_REQUESTED")
                .targetSystem("PAYMENT_SERVICE")
                .payload(serialize(order))
                .build());

        order.setCardHolderName(null);
        order.setCardNumber(null);
        order.setExpireMonth(null);
        order.setExpireYear(null);
        order.setCvc(null);

        orderRepositoryPort.save(order);
        log.info("Order status updated to STOCK_RESERVED. orderId={}", orderId);
    }

    @Override
    @Transactional
    public void handleStockReservationFailed(UUID orderId, String reason) {
        Order order = findWithLock(orderId);

        if (order.getStatus() != OrderStatus.PENDING) {
            log.warn("StockReservationFailedEvent ignored. orderId={}, status={}", orderId, order.getStatus());
            return;
        }

        order.setStatus(OrderStatus.CANCELLED);
        orderRepositoryPort.save(order);
        log.info("Order cancelled due to stock failure. orderId={}, reason={}", orderId, reason);
    }

    @Override
    @Transactional
    public void handlePaymentCompleted(UUID orderId, String sellerEmail) {
        Order order = findWithLock(orderId);

        if (order.getStatus() != OrderStatus.STOCK_RESERVED) {
            log.warn("PaymentCompletedEvent ignored. orderId={}, status={}", orderId, order.getStatus());
            return;
        }

        order.setStatus(OrderStatus.CONFIRMED);
        order.setSellerEmail(sellerEmail);
        orderRepositoryPort.save(order);

        outboxEventPort.save(OutboxMessage.builder()
                .aggregateId(orderId.toString())
                .aggregateType("ORDER")
                .eventType("ORDER_CONFIRMED")
                .targetSystem("INVENTORY_SERVICE")
                .payload(serialize(order))
                .build());

        outboxEventPort.save(OutboxMessage.builder()
                .aggregateId(orderId.toString())
                .aggregateType("ORDER")
                .eventType("ORDER_CONFIRMED")
                .targetSystem("NOTIFICATION_SERVICE")
                .payload(serialize(order))
                .build());

        log.info("Order confirmed. orderId={}", orderId);
    }

    @Override
    @Transactional
    public void handlePaymentFailed(UUID orderId, String reason) {
        Order order = findWithLock(orderId);

        if (order.getStatus() != OrderStatus.STOCK_RESERVED) {
            log.warn("PaymentFailedEvent ignored. orderId={}, status={}", orderId, order.getStatus());
            return;
        }

        order.setStatus(OrderStatus.CANCELLED);
        orderRepositoryPort.save(order);

        outboxEventPort.save(OutboxMessage.builder()
                .aggregateId(orderId.toString())
                .aggregateType("ORDER")
                .eventType("STOCK_RELEASED")
                .targetSystem("INVENTORY_SERVICE")
                .build());

        log.info("Order cancelled due to payment failure. orderId={}, reason={}", orderId, reason);
    }

    private Order findWithLock(UUID orderId) {
        return orderRepositoryPort.findByIdWithLock(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found. orderId: " + orderId));
    }

    private String serialize(Object payload) {
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize outbox payload.", e);
        }
    }

}

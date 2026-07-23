package org.murat.orderservicehexagonal.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import org.murat.orderservicehexagonal.application.port.in.command.CreateOrderCommand;
import org.murat.orderservicehexagonal.application.port.in.usecase.CreateOrderUseCase;
import org.murat.orderservicehexagonal.application.port.out.CartServicePort;
import org.murat.orderservicehexagonal.application.port.out.OrderRepositoryPort;
import org.murat.orderservicehexagonal.application.port.out.OutboxEventPort;
import org.murat.orderservicehexagonal.domain.enums.OrderStatus;
import org.murat.orderservicehexagonal.domain.event.OutboxMessage;
import org.murat.orderservicehexagonal.domain.exception.DuplicateOrderException;
import org.murat.orderservicehexagonal.domain.exception.EmptyCartException;
import org.murat.orderservicehexagonal.domain.model.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateOrderService implements CreateOrderUseCase {

    private final OrderRepositoryPort orderRepositoryPort;
    private final CartServicePort cartServicePort;
    private final OutboxEventPort outboxEventPort;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public Order createOrder(UUID userId, CreateOrderCommand command) {

        if (orderRepositoryPort.existsByUserIdAndStatus(userId, OrderStatus.PENDING)) {
            throw new DuplicateOrderException("You already have a pending order.");
        }

        Cart cart = cartServicePort.getCart(userId);

        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new EmptyCartException("Cannot create order with empty cart.");
        }

        List<OrderItem> orderItems = cart.getItems().stream()
                .map(this::toOrderItem)
                .toList();

        BigDecimal totalAmount = orderItems.stream()
                .map(OrderItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = Order.builder()
                .userId(userId)
                .buyerEmail(command.buyerEmail())
                .status(OrderStatus.PENDING)
                .totalAmount(totalAmount)
                .shippingAddress(command.shippingAddress())
                .cardHolderName(command.cardHolderName())
                .cardNumber(command.cardNumber())
                .expireMonth(command.expireMonth())
                .expireYear(command.expireYear())
                .cvc(command.cvc())
                .items(orderItems)
                .build();

        Order saved = orderRepositoryPort.save(order);

        outboxEventPort.save(OutboxMessage.builder()
                .aggregateId(saved.getId().toString())
                .aggregateType("ORDER")
                .eventType("ORDER_CREATED")
                .targetSystem("INVENTORY_SERVICE")
                .payload(serialize(saved))
                .build());

        try {
            cartServicePort.clearCart(userId);
        } catch (Exception e) {
            log.warn("Failed to clear cart. userId={}", userId);
        }

        log.info("Order created. orderId={}, userId={}, totalAmount={}",
                saved.getId(), userId, totalAmount);

        return saved;
    }

    private OrderItem toOrderItem(CartItem cartItem) {
        return OrderItem.builder()
                .productId(cartItem.getProductId())
                .productName(cartItem.getName())
                .quantity(cartItem.getQuantity())
                .unitPrice(cartItem.getPrice())
                .totalPrice(cartItem.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())))
                .sellerId(cartItem.getSellerId())
                .build();
    }

    private String serialize(Object payload) {
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize outbox payload.", e);
        }
    }
}

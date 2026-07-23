package org.murat.orderservicehexagonal.adapter.out.persistence.mapper;

import org.murat.orderservicehexagonal.adapter.out.persistence.entity.OrderItemJpaEntity;
import org.murat.orderservicehexagonal.adapter.out.persistence.entity.OrderJpaEntity;
import org.murat.orderservicehexagonal.domain.model.Order;
import org.murat.orderservicehexagonal.domain.model.OrderItem;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderPersistenceMapper {

    public OrderJpaEntity toEntity(Order order) {
        OrderJpaEntity entity = OrderJpaEntity.builder()
                .id(order.getId())
                .userId(order.getUserId())
                .buyerEmail(order.getBuyerEmail())
                .sellerEmail(order.getSellerEmail())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .shippingAddress(order.getShippingAddress())
                .cardHolderName(order.getCardHolderName())
                .cardNumber(order.getCardNumber())
                .expireMonth(order.getExpireMonth())
                .expireYear(order.getExpireYear())
                .cvc(order.getCvc())
                .build();

        List<OrderItemJpaEntity> itemEntities = order.getItems().stream()
                .map(item -> toItemEntity(item, entity))
                .toList();

        entity.setItems(itemEntities);
        return entity;
    }

    public Order toDomain(OrderJpaEntity entity) {
        List<OrderItem> items = entity.getItems().stream()
                .map(this::toItemDomain)
                .toList();

        return Order.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .buyerEmail(entity.getBuyerEmail())
                .sellerEmail(entity.getSellerEmail())
                .status(entity.getStatus())
                .totalAmount(entity.getTotalAmount())
                .shippingAddress(entity.getShippingAddress())
                .cardHolderName(entity.getCardHolderName())
                .cardNumber(entity.getCardNumber())
                .expireMonth(entity.getExpireMonth())
                .expireYear(entity.getExpireYear())
                .cvc(entity.getCvc())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .version(entity.getVersion())
                .items(items)
                .build();
    }

    private OrderItemJpaEntity toItemEntity(OrderItem item, OrderJpaEntity orderEntity) {
        return OrderItemJpaEntity.builder()
                .id(item.getId())
                .order(orderEntity)
                .productId(item.getProductId())
                .sellerId(item.getSellerId())
                .productName(item.getProductName())
                .quantity(item.getQuantity())
                .unitPrice(item.getUnitPrice())
                .totalPrice(item.getTotalPrice())
                .build();
    }

    private OrderItem toItemDomain(OrderItemJpaEntity entity) {
        return OrderItem.builder()
                .id(entity.getId())
                .orderId(entity.getOrder().getId())
                .productId(entity.getProductId())
                .sellerId(entity.getSellerId())
                .productName(entity.getProductName())
                .quantity(entity.getQuantity())
                .unitPrice(entity.getUnitPrice())
                .totalPrice(entity.getTotalPrice())
                .build();
    }
}

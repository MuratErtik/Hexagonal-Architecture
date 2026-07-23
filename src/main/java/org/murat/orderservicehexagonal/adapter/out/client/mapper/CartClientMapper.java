package org.murat.orderservicehexagonal.adapter.out.client.mapper;

import org.murat.orderservicehexagonal.adapter.out.client.dto.CartItemResponse;
import org.murat.orderservicehexagonal.adapter.out.client.dto.CartResponse;
import org.murat.orderservicehexagonal.domain.model.Cart;
import org.murat.orderservicehexagonal.domain.model.CartItem;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CartClientMapper {

    public Cart toDomain(CartResponse response) {
        List<CartItem> items = response.getItems().stream()
                .map(this::toItemDomain)
                .toList();

        return Cart.builder()
                .userId(response.getUserId())
                .items(items)
                .build();
    }

    private CartItem toItemDomain(CartItemResponse response) {
        return CartItem.builder()
                .productId(response.getProductId())
                .name(response.getName())
                .price(response.getPrice())
                .quantity(response.getQuantity())
                .imageUrl(response.getImageUrl())
                .sellerId(response.getSellerId())
                .build();
    }
}
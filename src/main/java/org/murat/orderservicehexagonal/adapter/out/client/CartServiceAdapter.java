package org.murat.orderservicehexagonal.adapter.out.client;



import lombok.RequiredArgsConstructor;
import org.murat.orderservicehexagonal.adapter.out.client.dto.CartResponse;
import org.murat.orderservicehexagonal.adapter.out.client.mapper.CartClientMapper;
import org.murat.orderservicehexagonal.application.port.out.CartServicePort;
import org.murat.orderservicehexagonal.domain.model.Cart;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CartServiceAdapter implements CartServicePort {

    private final CartFeignClient cartFeignClient;

    private final CartClientMapper mapper;

    @Override
    public Cart getCart(UUID userId) {
        CartResponse response = cartFeignClient.getCart(userId.toString());
        return mapper.toDomain(response);
    }

    @Override
    public void clearCart(UUID userId) {
        cartFeignClient.clearCart(userId.toString());
    }
}

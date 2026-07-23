package org.murat.orderservicehexagonal.adapter.out.client;

import org.murat.orderservicehexagonal.adapter.out.client.dto.CartResponse;
import org.murat.orderservicehexagonal.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "cart-service",
        url = "${feign.clients.cart-service.url}",
        configuration = FeignConfig.class
)
public interface CartFeignClient {

    @GetMapping("/api/v1/cart")
    CartResponse getCart(@RequestHeader("X-User-ID") String userId);

    @DeleteMapping("/api/v1/cart")
    void clearCart(@RequestHeader("X-User-ID") String userId);
}



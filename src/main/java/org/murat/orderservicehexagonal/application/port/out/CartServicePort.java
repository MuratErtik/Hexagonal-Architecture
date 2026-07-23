package org.murat.orderservicehexagonal.application.port.out;

import org.murat.orderservicehexagonal.domain.model.Cart;

import java.util.UUID;

public interface CartServicePort {

    Cart getCart(UUID userId);

    void clearCart(UUID userId);

}

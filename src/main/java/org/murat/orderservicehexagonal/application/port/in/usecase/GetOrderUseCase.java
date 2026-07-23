package org.murat.orderservicehexagonal.application.port.in.usecase;


import org.murat.orderservicehexagonal.domain.model.Order;

import java.util.UUID;

public interface GetOrderUseCase {

    Order getOrder(UUID userId, UUID orderId);
}






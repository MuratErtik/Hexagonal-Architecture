package org.murat.orderservicehexagonal.application.port.in.usecase;

import org.murat.orderservicehexagonal.application.port.in.command.CreateOrderCommand;
import org.murat.orderservicehexagonal.domain.model.Order;

import java.util.UUID;

public interface CreateOrderUseCase {

    Order createOrder(UUID userId, CreateOrderCommand command);
}

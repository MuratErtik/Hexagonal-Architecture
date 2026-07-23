package org.murat.orderservicehexagonal.application.port.in.usecase;


import java.util.UUID;

public interface HandlePaymentFailedUseCase {
    void handlePaymentFailed(UUID orderId, String reason);
}

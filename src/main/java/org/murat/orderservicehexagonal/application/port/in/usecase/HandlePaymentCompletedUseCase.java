package org.murat.orderservicehexagonal.application.port.in.usecase;


import java.util.UUID;

public interface HandlePaymentCompletedUseCase {
    void handlePaymentCompleted(UUID orderId, String sellerEmail);
}

package org.murat.orderservicehexagonal.application.port.in.usecase;


import java.util.UUID;

public interface HandleStockReservationFailedUseCase {
    void handleStockReservationFailed(UUID orderId, String reason);
}

package org.murat.orderservicehexagonal.application.port.in.usecase;


import java.util.UUID;

public interface HandleStockReservedUseCase {
    void handleStockReserved(UUID orderId);
}

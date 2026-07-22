package org.murat.orderservicehexagonal.domain.event;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockReservationFailedEvent {
    private UUID orderId;
    private UUID productId;
    private String reason;
}

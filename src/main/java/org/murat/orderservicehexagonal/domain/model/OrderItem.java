package org.murat.orderservicehexagonal.domain.model;




import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {

    private UUID id;

    private Long orderId;

    private UUID productId;

    private UUID sellerId;

    private String productName;

    private Integer quantity;

    private BigDecimal unitPrice;

    private BigDecimal totalPrice;
}

package org.murat.orderservicehexagonal.domain.model;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cart {

    private UUID userId;
    private List<CartItem> items;
    private BigDecimal totalPrice;
    private Integer totalItems;
}

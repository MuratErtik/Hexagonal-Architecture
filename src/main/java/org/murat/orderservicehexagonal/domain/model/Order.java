package org.murat.orderservicehexagonal.domain.model;

import lombok.*;
import org.murat.orderservicehexagonal.domain.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Order {


    private UUID id;

    private UUID userId;

    private String buyerEmail;

    private String sellerEmail;

    private OrderStatus status;

    private BigDecimal totalAmount;

    private String shippingAddress;

    private String cardHolderName;

    private String cardNumber;

    private String expireMonth;

    private String expireYear;

    private String cvc;

    @Builder.Default
    private List<OrderItem> items = new ArrayList<>();

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Long version;
}

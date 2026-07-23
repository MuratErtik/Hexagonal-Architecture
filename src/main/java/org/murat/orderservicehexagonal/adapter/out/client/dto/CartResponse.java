package org.murat.orderservicehexagonal.adapter.out.client.dto;

import lombok.Data;
import java.util.List;
import java.util.UUID;

@Data
public class CartResponse {
    private UUID userId;
    private List<CartItemResponse> items;
}



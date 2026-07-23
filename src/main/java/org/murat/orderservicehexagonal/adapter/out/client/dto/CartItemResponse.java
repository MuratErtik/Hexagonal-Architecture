package org.murat.orderservicehexagonal.adapter.out.client.dto;



import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class CartItemResponse {
    private UUID productId;
    private String name;
    private BigDecimal price;
    private Integer quantity;
    private String imageUrl;
    private UUID sellerId;
}

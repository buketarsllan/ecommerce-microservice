package com.example.order.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemResponse {
    private Long productId;
    private String name;
    private Integer quantity;
    private java.math.BigDecimal price;
}

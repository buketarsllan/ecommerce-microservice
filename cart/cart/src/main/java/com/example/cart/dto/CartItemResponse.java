package com.example.cart.dto;

import lombok.*;

import java.math.BigDecimal;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemResponse {
    private Long productId;
    private String name;
    private BigDecimal price;
    private Integer quantity;
}


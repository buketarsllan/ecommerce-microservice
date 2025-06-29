package com.example.cart.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddToCartRequest {
    private String userId;
    private Long productId;
    private Integer quantity;
}

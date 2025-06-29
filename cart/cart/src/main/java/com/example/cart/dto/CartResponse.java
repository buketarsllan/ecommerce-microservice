package com.example.cart.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartResponse {
    private Long id;
    private String userId;
    private List<CartItemResponse> items;
}

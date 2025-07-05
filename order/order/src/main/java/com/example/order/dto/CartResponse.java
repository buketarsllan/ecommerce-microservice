package com.example.order.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartResponse {
    private Long id;
    private String userId;
    private List<CartItemResponse> items;
}

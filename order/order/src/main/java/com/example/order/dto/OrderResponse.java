package com.example.order.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {
    private Long orderId;
    private String userId;
    private LocalDateTime orderDate;
    private List<OrderItemResponse> items;
}

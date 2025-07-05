package com.example.order.service;

import com.example.order.dto.*;
import com.example.order.entity.*;
import com.example.order.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final RestTemplate restTemplate;

    public OrderService(OrderRepository orderRepository, RestTemplate restTemplate) {
        this.orderRepository = orderRepository;
        this.restTemplate = restTemplate;
    }

    public OrderResponse placeOrder(PlaceOrderRequest request) {
        //  Call Cart Service
        String cartServiceUrl = "http://localhost:8082/api/cart/" + request.getUserId();
        CartResponse cart = restTemplate.getForObject(cartServiceUrl, CartResponse.class);

        List<OrderItem> orderItems = cart.getItems().stream().map(item ->
                OrderItem.builder()
                        .productId(item.getProductId())
                        .productName(item.getName())
                        .quantity(item.getQuantity())
                        .price(item.getPrice())
                        .build()
        ).toList();

        // Build order
        Order order = Order.builder()
                .userId(request.getUserId())
                .orderDate(LocalDateTime.now())
                .items(orderItems)
                .build();

        orderItems.forEach(i -> i.setOrder(order));

        //  Save order
        Order saved = orderRepository.save(order);

        //Call to Cart Service to clear cart after order is placed
        String clearCartUrl = "http://localhost:8082/api/cart/delete/" + request.getUserId();
        restTemplate.delete(clearCartUrl);  // Delete items from cart

        return mapToResponse(saved);
    }

    public List<OrderResponse> getOrdersByUser(String userId) {
        return orderRepository.findByUserId(userId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    private OrderResponse mapToResponse(Order order) {
        return OrderResponse.builder()
                .orderId(order.getId())
                .userId(order.getUserId())
                .orderDate(order.getOrderDate())
                .items(order.getItems().stream().map(item -> OrderItemResponse.builder()
                        .productId(item.getProductId())
                        .productName(item.getProductName())
                        .quantity(item.getQuantity())
                        .price(item.getPrice())
                        .build()).toList())
                .build();
    }
}

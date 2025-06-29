package com.example.cart.service;

import com.example.cart.dto.*;
import com.example.cart.entity.*;
import com.example.cart.repository.*;
import com.example.cart.config.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.time.LocalDateTime;
import java.util.List;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final RestTemplate restTemplate;

    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository, RestTemplate restTemplate) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.restTemplate = restTemplate;
    }

    public CartResponse addToCart(AddToCartRequest request) {
        Cart cart = cartRepository.findByUserId(request.getUserId())
                .orElse(Cart.builder()
                        .userId(request.getUserId())
                        .createdAt(LocalDateTime.now())
                        .build());

        CartItem item = CartItem.builder()
                .productId(request.getProductId())
                .quantity(request.getQuantity())
                .cart(cart)
                .build();

        cart.getItems().add(item);
        Cart savedCart = cartRepository.save(cart);

        return mapToCartResponse(savedCart);
    }

    public CartResponse getCartByUserId(String userId) {
        Cart cart = cartRepository.findByUserId(userId).orElseThrow();
        return mapToCartResponse(cart);
    }

    private CartResponse mapToCartResponse(Cart cart) {
        List<CartItemResponse> itemResponses = cart.getItems().stream()
                .map(item -> {
                    ProductResponse product = restTemplate.getForObject(
                            "http://localhost:8081/api/products/" + item.getProductId(),
                            ProductResponse.class
                    );

                    return CartItemResponse.builder()
                            .productId(item.getProductId())
                            .quantity(item.getQuantity())
                            .name(product != null ? product.getName() : "N/A")
                            .price(product != null ? product.getPrice() : null)
                            .build();
                })
                .toList();

        return CartResponse.builder()
                .id(cart.getId())
                .userId(cart.getUserId())
                .items(itemResponses)
                .build();
    }

    public CartResponse updateCartItem(UpdateCartItemRequest request) {
        Cart cart = cartRepository.findByUserId(request.getUserId())
                .orElseThrow();

        cart.getItems().forEach(item -> {
            if (item.getProductId().equals(request.getProductId())) {
                item.setQuantity(request.getQuantity());
            }
        });

        Cart updatedCart = cartRepository.save(cart);
        return mapToCartResponse(updatedCart);
    }

    public CartResponse deleteCartItem(String userId, Long productId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow();

        cart.getItems().removeIf(item -> item.getProductId().equals(productId));

        Cart updatedCart = cartRepository.save(cart);
        return mapToCartResponse(updatedCart);
    }


}

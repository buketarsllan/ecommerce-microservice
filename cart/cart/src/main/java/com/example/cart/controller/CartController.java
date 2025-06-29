package com.example.cart.controller;

import com.example.cart.dto.*;
import com.example.cart.service.CartService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/add")
    public CartResponse addToCart(@RequestBody AddToCartRequest request) {
        return cartService.addToCart(request);
    }

    @GetMapping("/{userId}")
    public CartResponse getCart(@PathVariable String userId) {
        return cartService.getCartByUserId(userId);
    }

    @PutMapping("/update")
    public CartResponse updateCartItem(@RequestBody UpdateCartItemRequest request) {
        return cartService.updateCartItem(request);
    }

    @DeleteMapping("/delete/{userId}/{productId}")
    public CartResponse deleteCartItem(@PathVariable String userId, @PathVariable Long productId) {
        return cartService.deleteCartItem(userId, productId);
    }


}


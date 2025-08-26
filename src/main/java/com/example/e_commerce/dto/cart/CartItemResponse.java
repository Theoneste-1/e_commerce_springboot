package com.example.e_commerce.dto.cart;


public record CartItemResponse(
        String id,
        String cartId,
        String productId,
        Integer quantity) {}
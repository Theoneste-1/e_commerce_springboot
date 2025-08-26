package com.example.e_commerce.dto.order;


public record OrderItemResponse(
        String id,
        String orderId,
        String productId,
        Integer quantity,
        Double price) {}
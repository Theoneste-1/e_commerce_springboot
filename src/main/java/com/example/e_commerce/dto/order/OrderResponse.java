package com.example.e_commerce.dto.order;


import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        String id,
        String userId,
        String status,
        Double totalAmount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<OrderItemResponse> orderItems) {

}

package com.example.e_commerce.dto.order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record OrderCreateRequest(
        @NotBlank(message = "User ID is required") String userId,
        @NotBlank(message = "Status is required") String status,
        @NotNull(message = "Total amount is required")
        @Positive(message = "Total amount must be positive") Double totalAmount,
        @NotNull(message = "Order items are required")
        List<OrderItemCreateRequest> orderItems) {}
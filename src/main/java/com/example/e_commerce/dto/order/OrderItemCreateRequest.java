package com.example.e_commerce.dto.order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OrderItemCreateRequest(
        @NotBlank(message = "Product ID is required") String productId,
        @NotNull(message = "Quantity is required")
        @Positive(message = "Quantity must be positive") Integer quantity,
        @NotNull(message = "Price is required")
        @Positive(message = "Price must be positive") Double price) {}

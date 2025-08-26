package com.example.e_commerce.dto.cart;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CartItemCreateRequest(
        @NotBlank(message = "Product ID is required") String productId,
        @NotNull(message = "Quantity is required")
        @Positive(message = "Quantity must be positive") Integer quantity) {}

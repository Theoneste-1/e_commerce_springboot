package com.example.e_commerce.dto.reviews;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReviewCreateRequest(
        @NotBlank(message = "Product ID is required") String productId,
        @NotBlank(message = "User ID is required") String userId,
        @NotNull(message = "Rating is required")
        @Min(value = 1, message = "Rating must be at least 1")
        @Max(value = 5, message = "Rating cannot exceed 5") Integer rating,
        String comment) {}


package com.example.e_commerce.dto.order;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OrderUpdateRequest(
        @NotBlank(message = "Status is required") String status,
        @NotNull(message = "Total amount is required")
        @Positive(message = "Total amount must be positive") Double totalAmount) {}

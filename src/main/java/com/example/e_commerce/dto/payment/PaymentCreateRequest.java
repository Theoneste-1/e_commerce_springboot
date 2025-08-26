package com.example.e_commerce.dto.payment;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PaymentCreateRequest(
        @NotBlank(message = "Order ID is required") String orderId,
        @NotNull(message = "Amount is required")
        @Positive(message = "Amount must be positive") Double amount,
        @NotBlank(message = "Payment method is required") String method,
        @NotBlank(message = "Status is required") String status,
        String transactionId) {}

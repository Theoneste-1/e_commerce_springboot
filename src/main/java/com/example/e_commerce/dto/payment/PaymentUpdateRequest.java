package com.example.e_commerce.dto.payment;


import jakarta.validation.constraints.NotBlank;

public record PaymentUpdateRequest(
        @NotBlank(message = "Status is required") String status,
        String transactionId) {}

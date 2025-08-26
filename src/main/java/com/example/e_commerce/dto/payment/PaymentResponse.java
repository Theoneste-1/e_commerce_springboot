package com.example.e_commerce.dto.payment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;

public record PaymentResponse(
        String id,
        String orderId,
        Double amount,
        String method,
        String status,
        String transactionId,
        LocalDateTime createdAt) {}
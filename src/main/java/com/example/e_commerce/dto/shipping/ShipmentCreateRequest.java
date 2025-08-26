package com.example.e_commerce.dto.shipping;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record ShipmentCreateRequest(
        @NotBlank(message = "Order ID is required") String orderId,
        String trackingNumber,
        String carrier,
        @NotBlank(message = "Status is required") String status,
        LocalDateTime shippedDate,
        LocalDateTime deliveryDate,
        @NotBlank(message = "Address ID is required") String addressId) {}

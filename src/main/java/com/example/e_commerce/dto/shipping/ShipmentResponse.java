package com.example.e_commerce.dto.shipping;

import java.time.LocalDateTime;


public record ShipmentResponse(
        String id,
        String orderId,
        String trackingNumber,
        String carrier,
        String status,
        LocalDateTime shippedDate,
        LocalDateTime deliveryDate,
        String addressId) {}

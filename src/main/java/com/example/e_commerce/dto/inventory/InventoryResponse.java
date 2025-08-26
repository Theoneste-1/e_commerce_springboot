package com.example.e_commerce.dto.inventory;

import java.time.LocalDateTime;


public record InventoryResponse(
        String id,
        String productId,
        Integer quantity,
        String location,
        LocalDateTime lastUpdated
) {}
package com.example.e_commerce.dto.product;

import java.time.LocalDateTime;


public record ProductResponse(
        String id,
        String name,
        String description,
        Double price,
        Integer stock,
        String categoryId,
        String brandId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {}
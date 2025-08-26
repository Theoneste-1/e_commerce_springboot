package com.example.e_commerce.dto.reviews;


import java.time.LocalDateTime;

public record ReviewResponse(
        String id,
        String productId,
        String userId,
        Integer rating,
        String comment,
        LocalDateTime createdAt) {}
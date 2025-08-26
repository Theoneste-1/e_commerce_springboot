package com.example.e_commerce.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record ProductCreateRequest(
        @NotBlank String name,
        String description,
        @Positive Double price,
        @Positive Integer stock,
        String categoryId,
        String brandId) {}

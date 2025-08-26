package com.example.e_commerce.dto.category_brand;

import jakarta.validation.constraints.NotBlank;

public record BrandCreateRequest(
        @NotBlank(message = "Brand name is required") String name,
        String description) {}

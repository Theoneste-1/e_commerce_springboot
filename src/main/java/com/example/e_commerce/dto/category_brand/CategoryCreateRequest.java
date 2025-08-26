package com.example.e_commerce.dto.category_brand;

import jakarta.validation.constraints.NotBlank;

public record CategoryCreateRequest(
        @NotBlank(message = "Category name is required") String name,
        String description,
        String parentCategoryId) {}

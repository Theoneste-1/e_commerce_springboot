package com.example.e_commerce.dto.category_brand;

public record CategoryUpdateRequest(
        String name,
        String description,
        String parentCategoryId) {}

package com.example.e_commerce.dto.cart;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CartUpdateRequest(
        @NotNull(message = "Items list is required")
        List<CartItemCreateRequest> items) {} // For adding/updating items


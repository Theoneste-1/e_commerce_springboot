package com.example.e_commerce.dto.wishlist;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record WishlistUpdateRequest(
        @NotNull(message = "Items list is required")
        List<WishlistItemCreateRequest> items) {} // For adding items


package com.example.e_commerce.dto.wishlist;

import jakarta.validation.constraints.NotBlank;


public record WishlistCreateRequest(
        @NotBlank(message = "User ID is required") String userId) {}



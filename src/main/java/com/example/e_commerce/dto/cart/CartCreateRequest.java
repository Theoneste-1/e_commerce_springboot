package com.example.e_commerce.dto.cart;

import jakarta.validation.constraints.NotBlank;


public record CartCreateRequest(
        @NotBlank(message = "User ID is required") String userId) {}

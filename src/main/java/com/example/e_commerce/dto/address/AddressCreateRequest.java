package com.example.e_commerce.dto.address;

import jakarta.validation.constraints.NotBlank;

public record AddressCreateRequest(
        @NotBlank(message = "User ID is required") String userId,
        @NotBlank(message = "Street is required") String street,
        @NotBlank(message = "City is required") String city,
        @NotBlank(message = "State is required") String state,
        @NotBlank(message = "Country is required") String country,
        @NotBlank(message = "Postal code is required") String postalCode,
        String phone) {}

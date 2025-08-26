package com.example.e_commerce.dto.address;

public record AddressResponse(
        String id,
        String userId,
        String street,
        String city,
        String state,
        String country,
        String postalCode,
        String phone) {}
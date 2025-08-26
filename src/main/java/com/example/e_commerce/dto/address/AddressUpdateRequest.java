package com.example.e_commerce.dto.address;


public record AddressUpdateRequest(
        String street,
        String city,
        String state,
        String country,
        String postalCode,
        String phone) {}

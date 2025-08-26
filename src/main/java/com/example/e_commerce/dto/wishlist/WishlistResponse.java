package com.example.e_commerce.dto.wishlist;

import java.time.LocalDateTime;
import java.util.List;

public record WishlistResponse(
        String id,
        String userId,
        LocalDateTime createdAt,
        List<WishlistItemResponse> wishlistItems) {}

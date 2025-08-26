package com.example.e_commerce.repositories.wishlist;

import com.example.e_commerce.models.wishlist.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface WishlistRepository extends JpaRepository<Wishlist, String> {
    // WishlistRepository
    @Query(value = "SELECT * FROM wishlists WHERE id = ?1", nativeQuery = true)
    Wishlist findByIdNative(String id);

    @Query(value = "SELECT * FROM wishlists WHERE user_id = ?1", nativeQuery = true)
    Wishlist findByUserId(String userId);
}

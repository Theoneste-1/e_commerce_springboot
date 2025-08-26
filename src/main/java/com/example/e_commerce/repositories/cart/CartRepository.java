package com.example.e_commerce.repositories.cart;

import com.example.e_commerce.models.cart.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CartRepository extends JpaRepository<Cart, String> {
    // CartRepository
    @Query(value = "SELECT * FROM carts WHERE id = ?1", nativeQuery = true)
    Cart findByIdNative(String id);

    @Query(value = "SELECT * FROM carts WHERE user_id = ?1", nativeQuery = true)
    Cart findByUserId(String userId);
}

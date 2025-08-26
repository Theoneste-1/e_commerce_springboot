package com.example.e_commerce.repositories.order;

import com.example.e_commerce.models.order.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, String> {
    // OrderRepository
    @Query(value = "SELECT * FROM orders WHERE id = ?1", nativeQuery = true)
    Order findByIdNative(String id);

    @Query(value = "SELECT * FROM orders WHERE user_id = ?1", nativeQuery = true)
    Page<Order> findByUserId(String userId, Pageable pageable);
}

package com.example.e_commerce.repositories.product;

import com.example.e_commerce.models.product.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    // InventoryRepository
    @Query(value = "SELECT * FROM inventories WHERE id = ?1", nativeQuery = true)
    Inventory findByIdNative(String id);

    @Query(value = "SELECT * FROM inventories WHERE product_id = ?1", nativeQuery = true)
    Inventory findByProductId(String productId);
}

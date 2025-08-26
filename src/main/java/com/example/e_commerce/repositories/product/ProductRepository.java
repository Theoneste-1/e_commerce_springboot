package com.example.e_commerce.repositories.product;

import com.example.e_commerce.models.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

    @Query(value = "SELECT * FROM products WHERE id = ?1", nativeQuery = true)
    Product findByIdNative(String id);

    @Query(value = "SELECT * FROM products WHERE category_id = ?1", nativeQuery = true)
    Page<Product> findByCategoryId(String categoryId, Pageable pageable);

    // Similar for brand, etc.
}
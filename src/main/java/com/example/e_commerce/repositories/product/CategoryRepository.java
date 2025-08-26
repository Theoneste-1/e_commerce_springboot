package com.example.e_commerce.repositories.product;

import com.example.e_commerce.models.product.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
    // CategoryRepository
    @Query(value = "SELECT * FROM categories WHERE id = ?1", nativeQuery = true)
    Category findByIdNative(String id);

    Page<Category> findByParentCategoryId(String parentId, Pageable pageable);
}
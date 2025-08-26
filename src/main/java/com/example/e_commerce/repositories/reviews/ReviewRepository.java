package com.example.e_commerce.repositories.reviews;

import com.example.e_commerce.models.reviews.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ReviewRepository extends CrudRepository<Review, Long> {
    // ReviewRepository
    @Query(value = "SELECT * FROM reviews WHERE id = ?1", nativeQuery = true)
    Review findByIdNative(String id);

    @Query(value = "SELECT * FROM reviews WHERE product_id = ?1", nativeQuery = true)
    Page<Review> findByProductId(String productId, Pageable pageable);

    @Query(value = "SELECT * FROM reviews WHERE user_id = ?1", nativeQuery = true)
    Page<Review> findByUserId(String userId, Pageable pageable);
}

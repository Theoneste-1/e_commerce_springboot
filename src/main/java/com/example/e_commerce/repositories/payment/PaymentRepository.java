package com.example.e_commerce.repositories.payment;

import com.example.e_commerce.models.payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

interface PaymentRepository extends JpaRepository<Payment, String> {
    // PaymentRepository
    @Query(value = "SELECT * FROM payments WHERE id = ?1", nativeQuery = true)
    Payment findByIdNative(String id);

    @Query(value = "SELECT * FROM payments WHERE order_id = ?1", nativeQuery = true)
    Payment findByOrderId(String orderId);
}
package com.example.e_commerce.models.payment;


import com.example.e_commerce.models.order.Order;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @UuidGenerator
    @Column(name = "id", columnDefinition = "varchar(36)")
    private String id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "method", nullable = false)
    private String method; // e.g., CREDIT_CARD, PAYPAL

    @Column(name = "status", nullable = false)
    private String status; // e.g., PAID, PENDING

    @Column(name = "transaction_id")
    private String transactionId;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
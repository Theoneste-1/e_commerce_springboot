package com.example.e_commerce.models.shipping;

import com.example.e_commerce.models.address.Address;
import com.example.e_commerce.models.order.Order;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;

@Entity
@Table(name = "shipments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Shipment {

    @Id
    @UuidGenerator
    @Column(name = "id", columnDefinition = "varchar(36)")
    private String id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order;

    @Column(name = "tracking_number")
    private String trackingNumber;

    @Column(name = "carrier")
    private String carrier;

    @Column(name = "status", nullable = false)
    private String status; // e.g., SHIPPED, DELIVERED

    @Column(name = "shipped_date")
    private LocalDateTime shippedDate;

    @Column(name = "delivery_date")
    private LocalDateTime deliveryDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;
}
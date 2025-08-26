package com.example.e_commerce.repositories.shipping;

import com.example.e_commerce.models.shipping.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ShipmentRepository extends JpaRepository<Shipment, String> {
    // ShipmentRepository
    @Query(value = "SELECT * FROM shipments WHERE id = ?1", nativeQuery = true)
    Shipment findByIdNative(String id);

    @Query(value = "SELECT * FROM shipments WHERE order_id = ?1", nativeQuery = true)
    Shipment findByOrderId(String orderId);
}

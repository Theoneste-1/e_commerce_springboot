package com.example.e_commerce.repositories.address;

import com.example.e_commerce.models.address.Address;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AddressRepository extends JpaRepository<Address, String> {
    // AddressRepository
    @Query(value = "SELECT * FROM addresses WHERE id = ?1", nativeQuery = true)
    Address findByIdNative(String id);

    @Query(value = "SELECT * FROM addresses WHERE user_id = ?1", nativeQuery = true)
    Page<Address> findByUserId(String userId, Pageable pageable);
}

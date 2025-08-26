package com.example.e_commerce.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;
import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product_brands")
public class Brand {
    @Id
    @UuidGenerator
    @Column(name = "id", nullable = false, columnDefinition = "varchar(50)")
    private String id;

    @Column(name = "brand_name", nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String description;

    @OneToMany
    private List<Product> products;

}

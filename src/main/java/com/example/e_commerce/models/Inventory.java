package com.example.e_commerce.models;

import jakarta.persistence.*;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "products_inventories")
public class Inventory {

    @Id
    @UuidGenerator
    @Column(name = "id", columnDefinition = "varchar(40)")
    private String id;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "inventory_products",
            joinColumns = @JoinColumn(
                    name = "inventory_id",
                    referencedColumnName = "id",
                    columnDefinition = "varchar(40)"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "product_id",
                    referencedColumnName = "id",
                    columnDefinition = "varchar(40)"
            )
    )
    private List<Product> products;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "inventory_location")
    private String location;

    @UpdateTimestamp
    @Column(name = "last_updated")
    private LocalDateTime lastUpdate;

}

package com.example.e_commerce.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product_categories")
public class Category {

    @Id
    @UuidGenerator
    @Column(name = "id" ,columnDefinition = "varchar(50)")
    private String id;

    @Column(name = "category_name", unique = true, nullable = false)
    private String name;

    @Column(name = "description", unique = true, nullable = false)
    private String description;
}

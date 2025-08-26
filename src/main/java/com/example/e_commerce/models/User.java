package com.example.e_commerce.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.*;
@Entity
@Table(name = "app_users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @UuidGenerator
    @Column(name = "id", columnDefinition = "varchar(36)")
    private String id;

    @Column(name = "username", length = 64, unique = true, nullable = false)
    private String username;

    @Column(name = "password", length = 100, nullable = false)
    private String password;

    @Column(name = "enabled", nullable = false)
    private Boolean enabled = true;

    @Column(name = "email", length = 254)
    private String email;

    @CreationTimestamp
    @Column(name="created_at", nullable = false, updatable = false)
    private LocalDateTime created_at;

    @ManyToMany(fetch = FetchType.LAZY)
            @JoinTable(
            name="app_user_roles",
                    joinColumns = @JoinColumn(
                            name = "user_id",
                            referencedColumnName = "id",
                            columnDefinition = "varchar(36)"   // 👈 force varchar(36)
                    ),
                    inverseJoinColumns = @JoinColumn(
                            name = "role_id",
                            referencedColumnName = "id",
                            columnDefinition = "varchar(36)"   // 👈 also varchar(36) if Role.id is String
                    )
    )
    private Set<Role> roles = new HashSet();

    public void addRole(Role role) {
        this.roles.add(role);
        role.getUsers().add(this);
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Constructor with all fields except id and createdAt
    public User(String username, String password, Boolean enabled, String email) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.email = email;
    }

    public void removeRole(Role role) {
        this.roles.remove(role);
        role.getUsers().remove(this);
    }

}

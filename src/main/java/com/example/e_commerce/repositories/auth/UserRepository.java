package com.example.e_commerce.repositories.auth;


import com.example.e_commerce.models.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.*;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    // Find by username
    Optional<User> findByUsername(String username);

    // Find by email
    Optional<User> findByEmail(String email);

    // Find by username or email
    @Query("SELECT u FROM User u WHERE u.username = :usernameOrEmail OR u.email = :usernameOrEmail")
    Optional<User> findByUsernameOrEmail(@Param("usernameOrEmail") String usernameOrEmail);

    // Check if username exists
    boolean existsByUsername(String username);

    // Check if email exists
    boolean existsByEmail(String email);

    // Find all enabled users
    List<User> findByEnabledTrue();

    // Find all disabled users
    List<User> findByEnabledFalse();

    // Find users by role name
    @Query("SELECT DISTINCT u FROM User u JOIN u.roles r WHERE r.name = :roleName")
    List<User> findByRoleName(@Param("roleName") String roleName);

    // Find users with specific role and enabled status
    @Query("SELECT DISTINCT u FROM User u JOIN u.roles r WHERE r.name = :roleName AND u.enabled = :enabled")
    List<User> findByRoleNameAndEnabled(@Param("roleName") String roleName, @Param("enabled") Boolean enabled);

    // Find users by username containing (case insensitive)
    List<User> findByUsernameContainingIgnoreCase(String username);

    // Count users by role
    @Query("SELECT COUNT(DISTINCT u) FROM User u JOIN u.roles r WHERE r.name = :roleName")
    Long countByRoleName(@Param("roleName") String roleName);

    // Find user with roles eagerly loaded
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.id = :id")
    Optional<User> findByIdWithRoles(@Param("id") String id);

    // Find user by username with roles eagerly loaded
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.username = :username")
    Optional<User> findByUsernameWithRoles(@Param("username") String username);
}

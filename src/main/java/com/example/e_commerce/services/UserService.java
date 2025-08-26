package com.example.e_commerce.services;


import com.example.e_commerce.config.PasswordEncoder;
import com.example.e_commerce.models.auth.Role;
import com.example.e_commerce.models.auth.User;
import com.example.e_commerce.repositories.auth.RoleRepository;
import com.example.e_commerce.repositories.auth.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

   @Autowired
   private PasswordEncoder passwordEncoder;

    public User createUser(String username, String password, String email) {
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encoder().encode(password));
        user.setEmail(email);
        user.setEnabled(true);

        // Add default role
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Default role not found"));
        user.addRole(userRole);

        return userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findByUsernameWithRoles(String username) {
        return userRepository.findByUsernameWithRoles(username);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public void assignRole(String userId, String roleName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        user.addRole(role);
        userRepository.save(user);
    }

    public void removeRole(String userId, String roleName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        user.removeRole(role);
        userRepository.save(user);
    }

    public void enableUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setEnabled(true);
        userRepository.save(user);
    }

    public void disableUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setEnabled(false);
        userRepository.save(user);
    }


}

package com.example.e_commerce.services;

import com.example.e_commerce.config.PasswordEncoder;
import com.example.e_commerce.dto.auth.SignupRequest;
import com.example.e_commerce.exceptions.UserAlreadyExistsException;
import com.example.e_commerce.models.auth.Role;
import com.example.e_commerce.models.auth.User;
import com.example.e_commerce.repositories.auth.RoleRepository;
import com.example.e_commerce.repositories.auth.UserRepository;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public void registerUser(SignupRequest signupRequest)  {
        try {

            // 1️⃣ Validate username
            if (signupRequest.getUsername() == null || signupRequest.getUsername().isBlank()) {
                throw new BadRequestException("Username is required and cannot be empty.");
            }
            if (signupRequest.getUsername().length() < 3 || signupRequest.getUsername().length() > 20) {
                throw new BadRequestException("Username must be between 3 and 20 characters.");
            }
            if (userRepository.existsByUsername(signupRequest.getUsername())) {
                throw new UserAlreadyExistsException("Username is already taken!");
            }

            // 2️⃣ Validate email
            if (signupRequest.getEmail() == null || signupRequest.getEmail().isBlank()) {
                throw new BadRequestException("Email is required and cannot be empty.");
            }
            if (!signupRequest.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                throw new BadRequestException("Email format is invalid.");
            }
            if (userRepository.existsByEmail(signupRequest.getEmail())) {
                throw new UserAlreadyExistsException("Email is already in use!");
            }

            // 3️⃣ Validate password
            if (signupRequest.getPassword() == null || signupRequest.getPassword().isBlank()) {
                throw new BadRequestException("Password is required.");
            }
            if (signupRequest.getPassword().length() < 6 || signupRequest.getPassword().length() > 40) {
                throw new BadRequestException("Password must be between 6 and 40 characters.");
            }

            // 4️⃣ Create user
            User user = new User();
            user.setUsername(signupRequest.getUsername());
            user.setEmail(signupRequest.getEmail());
            user.setPassword(passwordEncoder.encoder().encode(signupRequest.getPassword()));
            user.setEnabled(true);

            // 5️⃣ Assign roles
            Set<com.example.e_commerce.dto.auth.Role> strRoles = signupRequest.getRoles(); // assuming your SignupRequest has Set<String> roles
            Set<Role> roles = new HashSet<>();

            if (strRoles == null || strRoles.isEmpty()) {
                // Default role if none submitted
                Role defaultRole = roleRepository.findByName("ROLE_USER")
                        .orElseThrow(() -> new RuntimeException("Error: Role USER is not found."));
                roles.add(defaultRole);
            } else {
                for (com.example.e_commerce.dto.auth.Role roleName : strRoles) {
                    Role role = roleRepository.findByName(roleName.name())
                            .orElseThrow(() -> new BadRequestException("Error: Role " + roleName.name() + " is not found."));
                    roles.add(role);
                }
            }

            user.setRoles(roles);

            // 6️⃣ Save user
            userRepository.save(user);
        } catch (Exception e) {
            this.logger.error(e.getMessage());
        }
    }
}

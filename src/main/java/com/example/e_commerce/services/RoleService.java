package com.example.e_commerce.services;

import com.example.e_commerce.dto.auth.RoleRequest;
import com.example.e_commerce.models.auth.Role;
import com.example.e_commerce.repositories.auth.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public Role createRole(RoleRequest roleRequest) {
        if (roleRepository.existsByName(roleRequest.getName())) {
            throw new IllegalArgumentException("Role already exists: " + roleRequest.getName());
        }
        Role role = new Role();
        role.setName(roleRequest.getName());
        return roleRepository.save(role);
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Role getRoleById(String id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Role not found with id: " + id));
    }

    public void deleteRole(String id) {
        if (!roleRepository.existsById(id)) {
            throw new IllegalArgumentException("Role not found with id: " + id);
        }
        roleRepository.deleteById(id);
    }
}

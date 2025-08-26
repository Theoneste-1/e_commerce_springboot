package com.example.e_commerce.controllers.product;


import com.example.e_commerce.dto.inventory.InventoryCreateRequest;
import com.example.e_commerce.dto.inventory.InventoryUpdateRequest;
import com.example.e_commerce.models.product.Inventory;
import com.example.e_commerce.services.product.InventoryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventories")
public class InventoryController {
    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping
    public ResponseEntity<Inventory> createInventory(@RequestBody @Valid InventoryCreateRequest inventoryCreateRequest) {
        return null;
    }

    @GetMapping
    public List<Inventory> getInventory() {
        return List.of();
    }

    @PutMapping(":id")
    public ResponseEntity<Inventory> addInventory(@Valid @RequestBody InventoryUpdateRequest inventoryUpdateRequest) {
        return null;
    }

    @GetMapping(":id")
    public ResponseEntity<Inventory> getInventoryById(@PathVariable String id) {
        return null;
    }

    @DeleteMapping(":id")
    public ResponseEntity<Inventory> deleteInventoryById(@PathVariable String id) {
        return null;
    }
}
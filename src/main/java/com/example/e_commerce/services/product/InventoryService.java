package com.example.e_commerce.services.product;

import com.example.e_commerce.dto.inventory.InventoryCreateRequest;
import com.example.e_commerce.dto.inventory.InventoryResponse;
import com.example.e_commerce.exceptions.ResourceNotFoundException;
import com.example.e_commerce.models.product.Inventory;
import com.example.e_commerce.models.product.Product;
import com.example.e_commerce.repositories.product.InventoryRepository;
import com.example.e_commerce.repositories.product.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventoryService {
    private static final Logger logger = LoggerFactory.getLogger(InventoryService.class);
    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;

    public InventoryService(InventoryRepository inventoryRepository, ProductRepository productRepository) {
        this.inventoryRepository = inventoryRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public InventoryResponse createInventory(InventoryCreateRequest request) {
        Product product = productRepository.findByIdNative(request.productId());
        if (product == null) throw new ResourceNotFoundException("Product not found: " + request.productId());
        Inventory inventory = new Inventory();
        inventory.setProduct(product);
        inventory.setQuantity(request.quantity());
        inventory.setLocation(request.location());
        inventory = inventoryRepository.save(inventory);
        logger.info("Created inventory for product ID: {}", request.productId());
        return mapToResponse(inventory);
    }

    // Similar getById, getAll, update, delete
    private InventoryResponse mapToResponse(Inventory inventory) {
        return new InventoryResponse(
                inventory.getId(),
                inventory.getProduct().getId(),
                inventory.getQuantity(),
                inventory.getLocation(),
                inventory.getLastUpdated()
        );
    }
}
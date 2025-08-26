package com.example.e_commerce.services.product;

import com.example.e_commerce.dto.category_brand.BrandCreateRequest;
import com.example.e_commerce.dto.category_brand.BrandResponse;
import com.example.e_commerce.dto.category_brand.BrandUpdateRequest;
import com.example.e_commerce.exceptions.DuplicateResourceException;
import com.example.e_commerce.exceptions.ResourceNotFoundException;
import com.example.e_commerce.models.product.Brand;
import com.example.e_commerce.repositories.product.BrandRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class BrandService {

    private final BrandRepository brandRepository;

    public BrandResponse createBrand(BrandCreateRequest request) throws DuplicateResourceException {
        log.info("Creating new brand: {}", request.name());

        // Check if brand already exists
        if (brandRepository.existsByName(request.name())) {
            throw new DuplicateResourceException("Brand with name '" + request.name() + "' already exists");
        }

        try {
            Brand brand = new Brand();
            brand.setName(request.name());
            brand.setDescription(request.description());
            brand.setCreatedAt(LocalDateTime.now());
            brand.setUpdatedAt(LocalDateTime.now());

            Brand savedBrand = brandRepository.save(brand);
            log.info("Brand created successfully with ID: {}", savedBrand.getId());

            return mapToBrandResponse(savedBrand);

        } catch (DataIntegrityViolationException e) {
            log.error("Data integrity violation while creating brand: {}", request.name(), e);
            throw new DuplicateResourceException("Brand with name '" + request.name() + "' already exists");
        }
    }

    @Transactional(readOnly = true)
    public BrandResponse getBrandById(String id) {
        log.info("Fetching brand with ID: {}", id);

        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found with ID: " + id));

        return mapToBrandResponse(brand);
    }

    @Transactional(readOnly = true)
    public List<BrandResponse> getAllBrands() {
        log.info("Fetching all brands");

        return brandRepository.findAll().stream()
                .map(this::mapToBrandResponse)
                .toList();
    }

    public BrandResponse updateBrand(String id, BrandUpdateRequest request) throws DuplicateResourceException {
        log.info("Updating brand with ID: {}", id);

        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found with ID: " + id));

        // Check if new name conflicts with existing brand
        if (request.name() != null && !request.name().equals(brand.getName())) {
            if (brandRepository.existsByName(request.name())) {
                throw new DuplicateResourceException("Brand with name '" + request.name() + "' already exists");
            }
            brand.setName(request.name());
        }

        if (request.description() != null) {
            brand.setDescription(request.description());
        }

        brand.setUpdatedAt(LocalDateTime.now());

        try {
            Brand updatedBrand = brandRepository.save(brand);
            log.info("Brand updated successfully with ID: {}", id);

            return mapToBrandResponse(updatedBrand);

        } catch (DataIntegrityViolationException e) {
            log.error("Data integrity violation while updating brand: {}", id, e);
            throw new DuplicateResourceException("Brand with name '" + request.name() + "' already exists");
        }
    }

    public void deleteBrand(String id) {
        log.info("Deleting brand with ID: {}", id);

        try {
            if (!brandRepository.existsById(id)) {
                throw new ResourceNotFoundException("Brand not found with ID: " + id);
            }

            brandRepository.deleteById(id);
            log.info("Brand deleted successfully with ID: {}", id);

        } catch (EmptyResultDataAccessException e) {
            log.warn("Attempted to delete non-existent brand with ID: {}", id);
            throw new ResourceNotFoundException("Brand not found with ID: " + id);
        }
    }

    @Transactional(readOnly = true)
    public BrandResponse getBrandByName(String name) {
        log.info("Fetching brand by name: {}", name);

        Brand brand = brandRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found with name: " + name));

        return mapToBrandResponse(brand);
    }

    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return brandRepository.existsByName(name);
    }

    private BrandResponse mapToBrandResponse(Brand brand) {
        return new BrandResponse(
                brand.getId(),
                brand.getName(),
                brand.getDescription()
        );
    }
}
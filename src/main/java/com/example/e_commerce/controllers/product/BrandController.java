package com.example.e_commerce.controllers.product;

import com.example.e_commerce.dto.category_brand.BrandCreateRequest;
import com.example.e_commerce.dto.category_brand.BrandResponse;
import com.example.e_commerce.dto.category_brand.BrandUpdateRequest;
import com.example.e_commerce.exceptions.DuplicateResourceException;
import com.example.e_commerce.services.product.BrandService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/brands")
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;

    @PostMapping
    public ResponseEntity<BrandResponse> createBrand(@RequestBody @Valid BrandCreateRequest request) throws DuplicateResourceException {
        BrandResponse createdBrand = brandService.createBrand(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBrand);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BrandResponse> getBrandById(@PathVariable String id) {
        BrandResponse brand = brandService.getBrandById(id);
        return ResponseEntity.ok(brand);
    }

    @GetMapping
    public ResponseEntity<List<BrandResponse>> getAllBrands() {
        List<BrandResponse> brands = brandService.getAllBrands();
        return ResponseEntity.ok(brands);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<BrandResponse> getBrandByName(@PathVariable String name) {
        BrandResponse brand = brandService.getBrandByName(name);
        return ResponseEntity.ok(brand);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BrandResponse> updateBrand(
            @PathVariable String id,
            @RequestBody @Valid BrandUpdateRequest request) throws DuplicateResourceException {
        BrandResponse updatedBrand = brandService.updateBrand(id, request);
        return ResponseEntity.ok(updatedBrand);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBrand(@PathVariable String id) {
        brandService.deleteBrand(id);
        return ResponseEntity.noContent().build();
    }
}
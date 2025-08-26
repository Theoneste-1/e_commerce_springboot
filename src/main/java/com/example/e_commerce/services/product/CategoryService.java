package com.example.e_commerce.services.product;

import com.example.e_commerce.dto.category_brand.CategoryCreateRequest;
import com.example.e_commerce.dto.category_brand.CategoryResponse;
import com.example.e_commerce.dto.category_brand.CategoryUpdateRequest;
import com.example.e_commerce.exceptions.ResourceNotFoundException;
import com.example.e_commerce.models.product.Category;
import com.example.e_commerce.repositories.product.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public CategoryResponse createCategory(CategoryCreateRequest request) {
        Category category = new Category();
        category.setName(request.name());
        category.setDescription(request.description());
        if (request.parentCategoryId() != null) {
            Category parent = categoryRepository.findByIdNative(request.parentCategoryId());
            if (parent == null) throw new ResourceNotFoundException("Parent category not found: " + request.parentCategoryId());
            category.setParentCategory(parent);
        }
        category = categoryRepository.save(category);
        logger.info("Created category with ID: {}", category.getId());
        return mapToResponse(category);
    }

    @Transactional(readOnly = true)
    public CategoryResponse getCategoryById(String id) {
        Category category = categoryRepository.findByIdNative(id);
        if (category == null) throw new ResourceNotFoundException("Category not found: " + id);
        return mapToResponse(category);
    }

    @Transactional(readOnly = true)
    public Page<CategoryResponse> getAllCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable).map(this::mapToResponse);
    }

    @Transactional
    public CategoryResponse updateCategory(String id, CategoryUpdateRequest request) {
        Category category = categoryRepository.findByIdNative(id);
        if (category == null) throw new ResourceNotFoundException("Category not found: " + id);
        if (request.name() != null) category.setName(request.name());
        if (request.description() != null) category.setDescription(request.description());
        if (request.parentCategoryId() != null) {
            Category parent = categoryRepository.findByIdNative(request.parentCategoryId());
            if (parent == null) throw new ResourceNotFoundException("Parent category not found: " + request.parentCategoryId());
            category.setParentCategory(parent);
        }
        category = categoryRepository.save(category);
        logger.info("Updated category with ID: {}", id);
        return mapToResponse(category);
    }

    @Transactional
    public void deleteCategory(String id) {
        Category category = categoryRepository.findByIdNative(id);
        if (category == null) throw new ResourceNotFoundException("Category not found: " + id);
        categoryRepository.delete(category);
        logger.info("Deleted category with ID: {}", id);
    }

    private CategoryResponse mapToResponse(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.getParentCategory() != null ? category.getParentCategory().getId() : null
        );
    }
}
package com.example.e_commerce.services.product;


import com.example.e_commerce.dto.product.ProductCreateRequest;
import com.example.e_commerce.dto.product.ProductResponse;
import com.example.e_commerce.dto.product.ProductUpdateRequest;
import com.example.e_commerce.exceptions.ResourceNotFoundException;
import com.example.e_commerce.models.product.Brand;
import com.example.e_commerce.models.product.Category;
import com.example.e_commerce.models.product.Product;
import com.example.e_commerce.repositories.product.BrandRepository;
import com.example.e_commerce.repositories.product.CategoryRepository;
import com.example.e_commerce.repositories.product.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ProductService {


    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BrandRepository brandRepository;

    private final ProductRepository productRepository;
    // Inject other repos if needed (e.g., CategoryRepository for validation)

    public ProductService(ProductRepository productRepository /*, other repos */) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse createProduct(ProductCreateRequest request) {
        Product product = new Product();
        try {
            Category cat = categoryRepository.findById(request.categoryId())
                            .orElseThrow(()-> new ResourceNotFoundException("Category with that id doesn't exist"));
            product.setCategory(cat);

            Brand brand = brandRepository.findById(request.brandId())
                            .orElseThrow(()-> new ResourceNotFoundException("Brand with that id doesn't exist"));

            product.setBrand(brand);
            product.setStock(request.stock());
            product.setPrice(request.price());
            product.setName(request.name());
            product.setDescription(request.description());
            product.setCreatedAt(LocalDateTime.now());
            product = productRepository.save(product);
            logger.info("Created product with ID: {}", product.getId());

        }catch (Exception e) {
            logger.error(e.getMessage());
        }
        return mapToResponse(product);
    }


    @Transactional(readOnly = true)
    public ProductResponse getProductById(String id) {
        Product product = productRepository.findByIdNative(id);
        if (product == null) {
            throw new ResourceNotFoundException("Product not found with ID: " + id);
        }
        return mapToResponse(product);
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable).map(this::mapToResponse);
    }

    @Transactional
    public ProductResponse updateProduct(String id, ProductUpdateRequest request) {
        Product product = productRepository.findByIdNative(id);
        if (product == null) {
            throw new ResourceNotFoundException("Product not found with ID: " + id);
        }
        // Update fields
        product = productRepository.save(product);
        logger.info("Updated product with ID: {}", id);
        return mapToResponse(product);
    }

    @Transactional
    public void deleteProduct(String id) {
        Product product = productRepository.findByIdNative(id);
        if (product == null) {
            throw new ResourceNotFoundException("Product not found with ID: " + id);
        }
        productRepository.delete(product);
        logger.info("Deleted product with ID: {}", id);
        // Cascading handles children
    }

    private ProductResponse mapToResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getCategory().getId(),
                product.getBrand().getId(),
                product.getCreatedAt(),
                product.getUpdatedAt()

        );
    }
}

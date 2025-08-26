package com.example.e_commerce.services.product;


import com.example.e_commerce.dto.product.ProductCreateRequest;
import com.example.e_commerce.dto.product.ProductResponse;
import com.example.e_commerce.dto.product.ProductUpdateRequest;
import com.example.e_commerce.exceptions.ResourceNotFoundException;
import com.example.e_commerce.models.product.Product;
import com.example.e_commerce.repositories.product.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;
    // Inject other repos if needed (e.g., CategoryRepository for validation)

    public ProductService(ProductRepository productRepository /*, other repos */) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse createProduct(ProductCreateRequest request) {
        // Validate category/brand existence
        Product product = new Product();
        product.setName(request.name());
        // Set other fields, fetch category/brand
        product = productRepository.save(product);
        logger.info("Created product with ID: {}", product.getId());
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

package theo.dev.ecommerce.repositories.product;

import theo.dev.ecommerce.models.product.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brand, String> {
    Optional<Brand> findByName(String name);

    boolean existsByName(String name);
}

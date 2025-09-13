package theo.dev.ecommerce.repositories.product;

import theo.dev.ecommerce.models.product.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface InventoryRepository extends JpaRepository<Inventory, String> {
    // InventoryRepository
    @Query(value = "SELECT * FROM inventories WHERE id = ?1", nativeQuery = true)
    Inventory findByIdNative(String id);

    @Query(value = "SELECT * FROM inventories WHERE product_id = ?1", nativeQuery = true)
    Inventory findByProductId(String productId);
}

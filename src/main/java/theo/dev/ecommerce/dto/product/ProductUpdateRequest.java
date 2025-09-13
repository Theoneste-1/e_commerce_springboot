package theo.dev.ecommerce.dto.product;

public record ProductUpdateRequest(
        String name,
        String description,
        Double price,
        Integer stock,
        String categoryId,
        String brandId) {
}

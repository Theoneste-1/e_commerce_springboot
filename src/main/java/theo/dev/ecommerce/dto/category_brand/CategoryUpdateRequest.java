package theo.dev.ecommerce.dto.category_brand;

public record CategoryUpdateRequest(
        String name,
        String description,
        String parentCategoryId) {
}

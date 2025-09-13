package theo.dev.ecommerce.dto.wishlist;

import jakarta.validation.constraints.NotBlank;

public record WishlistItemCreateRequest(
        @NotBlank(message = "Product ID is required") String productId) {
}

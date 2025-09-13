package theo.dev.ecommerce.dto.wishlist;

public record WishlistItemResponse(
        String id,
        String wishlistId,
        String productId) {
}
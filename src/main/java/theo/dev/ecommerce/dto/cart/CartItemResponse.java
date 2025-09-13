package theo.dev.ecommerce.dto.cart;

public record CartItemResponse(
        String id,
        String cartId,
        String productId,
        Integer quantity) {
}
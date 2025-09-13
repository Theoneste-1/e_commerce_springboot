package theo.dev.ecommerce.dto.cart;

import java.time.LocalDateTime;
import java.util.List;

public record CartResponse(
        String id,
        String userId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<CartItemResponse> cartItems) {
}

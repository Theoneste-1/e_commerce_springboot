package theo.dev.ecommerce.dto.reviews;

import java.time.LocalDateTime;

public record ReviewResponse(
        String id,
        String productId,
        String userId,
        Integer rating,
        String comment,
        LocalDateTime createdAt) {
}
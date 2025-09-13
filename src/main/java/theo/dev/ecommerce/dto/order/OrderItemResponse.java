package theo.dev.ecommerce.dto.order;

public record OrderItemResponse(
        String id,
        String orderId,
        String productId,
        Integer quantity,
        Double price) {
}
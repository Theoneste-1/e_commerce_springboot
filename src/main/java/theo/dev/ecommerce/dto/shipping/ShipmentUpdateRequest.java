package theo.dev.ecommerce.dto.shipping;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record ShipmentUpdateRequest(
        String trackingNumber,
        String carrier,
        @NotBlank(message = "Status is required") String status,
        LocalDateTime shippedDate,
        LocalDateTime deliveryDate) {
}

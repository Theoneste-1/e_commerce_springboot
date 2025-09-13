package theo.dev.ecommerce.dto.address;

public record AddressUpdateRequest(
        String street,
        String city,
        String state,
        String country,
        String postalCode,
        String phone) {
}

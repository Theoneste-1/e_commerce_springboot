package theo.dev.ecommerce.auth;

import lombok.Data;

@Data

public class SignUpResponse {
    String message;

    public SignUpResponse(String message) {
        this.message = message;
    }
}

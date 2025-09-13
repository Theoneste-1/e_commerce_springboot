package theo.dev.ecommerce.repositories.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import theo.dev.ecommerce.models.auth.PasswordResetToken;
import theo.dev.ecommerce.models.auth.User;
import java.util.*;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, String> {
    Optional<PasswordResetToken> findByToken(String token);
}

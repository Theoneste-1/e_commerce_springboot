package theo.dev.ecommerce.repositories.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import theo.dev.ecommerce.models.auth.PasswordResetToken;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, String> {
}

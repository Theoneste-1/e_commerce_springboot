package theo.dev.ecommerce.models.auth;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "password_reset_tokens")
public class PasswordResetToken {

    @Id
    @UuidGenerator
    @Column(name = "id", columnDefinition = "varchar(40)")
    private String id;

    private String token;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime expiryDate;
}

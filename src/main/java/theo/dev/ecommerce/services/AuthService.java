package theo.dev.ecommerce.services;

import jakarta.validation.Valid;
import theo.dev.ecommerce.auth.ChangePasswordRequest;
import theo.dev.ecommerce.auth.ForgetPasswordRequest;
import theo.dev.ecommerce.auth.ResetPasswordRequest;
import theo.dev.ecommerce.auth.SignUpResponse;
import theo.dev.ecommerce.config.AppProperties;
import theo.dev.ecommerce.config.PasswordEncoder;
import theo.dev.ecommerce.dto.auth.SignupRequest;
import theo.dev.ecommerce.exceptions.ResourceNotFoundException;
import theo.dev.ecommerce.exceptions.UserAlreadyExistsException;
import theo.dev.ecommerce.models.auth.PasswordResetToken;
import theo.dev.ecommerce.models.auth.Role;
import theo.dev.ecommerce.models.auth.User;
import theo.dev.ecommerce.repositories.auth.PasswordResetTokenRepository;
import theo.dev.ecommerce.repositories.auth.RoleRepository;
import theo.dev.ecommerce.repositories.auth.UserRepository;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private AppProperties appProperties;

    @Transactional
    public void registerUser(SignupRequest signupRequest) {
        try {

            // 1️⃣ Validate username
            if (signupRequest.getUsername() == null || signupRequest.getUsername().isBlank()) {
                throw new BadRequestException("Username is required and cannot be empty.");
            }
            if (signupRequest.getUsername().length() < 3 || signupRequest.getUsername().length() > 20) {
                throw new BadRequestException("Username must be between 3 and 20 characters.");
            }
            if (userRepository.existsByUsername(signupRequest.getUsername())) {
                throw new UserAlreadyExistsException("Username is already taken!");
            }

            // 2️⃣ Validate email
            if (signupRequest.getEmail() == null || signupRequest.getEmail().isBlank()) {
                throw new BadRequestException("Email is required and cannot be empty.");
            }
            if (!signupRequest.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                throw new BadRequestException("Email format is invalid.");
            }
            if (userRepository.existsByEmail(signupRequest.getEmail())) {
                throw new UserAlreadyExistsException("Email is already in use!");
            }

            // 3️⃣ Validate password
            if (signupRequest.getPassword() == null || signupRequest.getPassword().isBlank()) {
                throw new BadRequestException("Password is required.");
            }
            if (signupRequest.getPassword().length() < 6 || signupRequest.getPassword().length() > 40) {
                throw new BadRequestException("Password must be between 6 and 40 characters.");
            }

            // 4️⃣ Create user
            User user = new User();
            user.setUsername(signupRequest.getUsername());
            user.setEmail(signupRequest.getEmail());
            user.setPassword(passwordEncoder.encoder().encode(signupRequest.getPassword()));
            user.setEnabled(true);

            // 5️⃣ Assign roles
            Set<theo.dev.ecommerce.dto.auth.Role> strRoles = signupRequest.getRoles(); // assuming your SignupRequest
                                                                                       // has Set<String> roles
            Set<Role> roles = new HashSet<>();

            if (strRoles == null || strRoles.isEmpty()) {
                // Default role if none submitted
                Role defaultRole = roleRepository.findByName("ROLE_USER")
                        .orElseThrow(() -> new RuntimeException("Error: Role USER is not found."));
                roles.add(defaultRole);
            } else {
                for (theo.dev.ecommerce.dto.auth.Role roleName : strRoles) {
                    Role role = roleRepository.findByName(roleName.name())
                            .orElseThrow(
                                    () -> new BadRequestException("Error: Role " + roleName.name() + " is not found."));
                    roles.add(role);
                }
            }

            user.setRoles(roles);

            // 6️⃣ Save user
            userRepository.save(user);

        } catch (Exception e) {
            this.logger.error(e.getMessage());
        }

    }



    public void requestPasswordReset(ForgetPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // generate token
        String token = UUID.randomUUID().toString();

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(30));
        tokenRepository.save(resetToken);

        // Send email
        String resetLink = appProperties.getFrontendUrl() + "/reset-password?token=" + token + "&email=" + user.getEmail();
        sendResetEmail(user.getEmail(), resetLink);
    }

    private void sendResetEmail(String to, String resetLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Password Reset Request");
        message.setText("Hello,\n\nClick the following link to reset your password:\n" + resetLink + "\n\nIf you didn't request this, ignore this email.");
        mailSender.send(message);
    }

    public void resetPassword(ResetPasswordRequest request) {
        PasswordResetToken tokenEntity = tokenRepository.findByToken(request.getToken())
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (tokenEntity.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expired");
        }

        User user = tokenEntity.getUser();
        if (!user.getEmail().equals(request.getEmail())) {
            throw new RuntimeException("Token does not match email");
        }

        user.setPassword(passwordEncoder.encoder().encode(request.getNewPassword()));
        userRepository.save(user);

        tokenRepository.delete(tokenEntity); // invalidate token
    }

    public void changePassword(User user, ChangePasswordRequest request) {
        if (!passwordEncoder.encoder().matches(request.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("Old password is incorrect");
        }

        user.setPassword(passwordEncoder.encoder().encode(request.getNewPassword()));
        userRepository.save(user);
    }
}

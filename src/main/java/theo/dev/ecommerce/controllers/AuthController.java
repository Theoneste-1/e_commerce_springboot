package theo.dev.ecommerce.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import theo.dev.ecommerce.auth.*;
import theo.dev.ecommerce.dto.auth.JwtResponse;
import theo.dev.ecommerce.dto.auth.LoginRequest;
import theo.dev.ecommerce.dto.auth.SignupRequest;
import theo.dev.ecommerce.security.CustomUserDetails;
import theo.dev.ecommerce.security.JWTUtil;
import theo.dev.ecommerce.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "User Management", description = "APIs for managing users")
public class AuthController {

        @Autowired
        private AuthenticationManager authenticationManager;

        @Autowired
        private JWTUtil jwtUtils;

        @Autowired
        private AuthService authService;



        @Operation(summary = "Login", description = "login with ur credentials")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "logged in successfull", content = @Content(schema = @Schema(implementation = JwtResponse.class))),
                        @ApiResponse(responseCode = "400", description = "invalid creds", content = @Content(schema = @Schema(implementation = MessageResponse.class))),
        })
        @PostMapping("/login")
        public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
                try {
                        Authentication authentication = authenticationManager.authenticate(
                                        new UsernamePasswordAuthenticationToken(
                                                        loginRequest.getUsernameOrEmail(),
                                                        loginRequest.getPassword()));

                        SecurityContextHolder.getContext().setAuthentication(authentication);

                        String accessToken = jwtUtils.generateJwtToken(authentication);
                        String refreshToken = jwtUtils.generateRefreshToken(authentication);

                        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

                        return ResponseEntity.ok(new JwtResponse(
                                        accessToken,
                                        refreshToken,
                                        userDetails.getId(),
                                        userDetails.getUsername(),
                                        userDetails.getEmail(),
                                        userDetails.getAuthorities().stream()
                                                        .map(authority -> authority.getAuthority())
                                                        .toList()));

                } catch (BadCredentialsException e) {
                        throw new BadCredentialsException("Invalid username/email or password");
                }
        }

        @Operation(summary = "create new user", description = "register new user or customer in the system")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "User created successfully", content = @Content(schema = @Schema(implementation = MessageResponse.class))),
                        @ApiResponse(responseCode = "409", description = "Conflict", content = @Content(schema = @Schema(implementation = MessageResponse.class)))
        })
        @PostMapping("/register")
        public ResponseEntity<MessageResponse> register(@Valid @RequestBody SignupRequest signupRequest) {

                authService.registerUser(signupRequest);
                return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
        }

        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "token refreshed success", content = @Content(schema = @Schema(implementation = TokenRefreshResponse.class))),
                        @ApiResponse(responseCode = "401", description = "invalid refresh token", content = @Content(schema = @Schema(implementation = MessageResponse.class)))
        })
        @PostMapping("/refresh")
        public ResponseEntity<TokenRefreshResponse> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
                String refreshToken = request.getRefreshToken();

                if (jwtUtils.validateJwtToken(refreshToken) && jwtUtils.isRefreshToken(refreshToken)) {
                        String userId = jwtUtils.getUserIdFromJwtToken(refreshToken);
                        String username = jwtUtils.getUsernameFromJwtToken(refreshToken);

                        String newAccessToken = jwtUtils.generateTokenFromUserId(userId, username, false);

                        return ResponseEntity.ok(new TokenRefreshResponse(newAccessToken, refreshToken));
                } else {
                        throw new RuntimeException("Invalid refresh token");
                }
        }

        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "logged out successfull")
        })
        @PostMapping("/logout")
        public ResponseEntity<MessageResponse> logout() {
                // In a real application, you might want to blacklist the token
                // For now, we'll just return a success message
                return ResponseEntity.ok(new MessageResponse("User logged out successfully!"));
        }


        @PostMapping("/forgot-password")
        public ResponseEntity<MessageResponse> forgotPassword(@Valid @RequestBody ForgetPasswordRequest request) {
                authService.requestPasswordReset(request);
                return ResponseEntity.ok(new MessageResponse("Password reset email sent"));
        }

        @PutMapping("/reset-password")
        public ResponseEntity<MessageResponse> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
                authService.resetPassword(request);
                return ResponseEntity.ok(new MessageResponse("Password has been reset successfully"));
        }

        @PutMapping("/change-password")
        @PreAuthorize("isAuthenticated()")
        public ResponseEntity<MessageResponse> changePassword(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                              @Valid @RequestBody ChangePasswordRequest request) {
                authService.changePassword(userDetails.getUser(), request);
                return ResponseEntity.ok(new MessageResponse("Password changed successfully"));
        }

}
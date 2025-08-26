package com.example.e_commerce.controllers;

import com.example.e_commerce.dto.auth.JwtResponse;
import com.example.e_commerce.dto.auth.LoginRequest;
import com.example.e_commerce.dto.auth.SignupRequest;
import com.example.e_commerce.security.CustomUserDetails;
import com.example.e_commerce.security.JWTUtil;
import com.example.e_commerce.services.AuthService;
import com.example.e_commerce.utils.auth.MessageResponse;
import com.example.e_commerce.utils.auth.TokenRefreshRequest;
import com.example.e_commerce.utils.auth.TokenRefreshResponse;
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
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTUtil jwtUtils;

    @Autowired
    private AuthService authService;

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
                            .toList()
            ));

        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid username/email or password");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<MessageResponse> register(@Valid @RequestBody SignupRequest signupRequest) {

        authService.registerUser(signupRequest);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

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

    @PostMapping("/logout")
    public ResponseEntity<MessageResponse> logout() {
        // In a real application, you might want to blacklist the token
        // For now, we'll just return a success message
        return ResponseEntity.ok(new MessageResponse("User logged out successfully!"));
    }
}
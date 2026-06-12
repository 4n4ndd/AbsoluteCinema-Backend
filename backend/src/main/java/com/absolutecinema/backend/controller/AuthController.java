package com.absolutecinema.backend.controller;

import com.absolutecinema.backend.dto.LoginRequest;
import com.absolutecinema.backend.dto.SignupRequest;
import com.absolutecinema.backend.model.User;
import com.absolutecinema.backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(
            @RequestBody SignupRequest request
    ) {
        return ResponseEntity.ok(
                authService.register(request)
        );
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequest loginRequest
    ) {
        String token = authService.login(loginRequest);

        if (token == null) {
            return ResponseEntity.badRequest()
                    .body("Invalid email or password");
        }

        if (token.equals("Please verify your email first")) {
            return ResponseEntity.badRequest()
                    .body(token);
        }

        User user = authService.getUserByEmail(
                loginRequest.getEmail()
        );

        Map<String, Object> response = new HashMap<>();

        response.put("token", token);
        response.put("userId", user.getId());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOTP(
            @RequestParam String email,
            @RequestParam String otp
    ) {
        return ResponseEntity.ok(
                authService.verifyOTP(email, otp)
        );
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<String> resendOTP(
            @RequestParam String email
    ) {
        return ResponseEntity.ok(
                authService.resendOTP(email)
        );
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(
            @RequestParam String email
    ) {
        return ResponseEntity.ok(
                authService.forgotPassword(email)
        );
    }

    @PostMapping("/verify-reset-otp")
    public ResponseEntity<String> verifyResetOTP(
            @RequestParam String email,
            @RequestParam String otp
    ) {
        return ResponseEntity.ok(
                authService.verifyResetOTP(email, otp)
        );
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @RequestParam String email,
            @RequestParam String newPassword
    ) {
        return ResponseEntity.ok(
                authService.resetPassword(email, newPassword)
        );
    }
}
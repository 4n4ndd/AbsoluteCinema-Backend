package com.absolutecinema.backend.controller;

import com.absolutecinema.backend.Repository.UserRepo;
import com.absolutecinema.backend.dto.LoginRequest;
import com.absolutecinema.backend.dto.SignupRequest;
import com.absolutecinema.backend.model.User;
import com.absolutecinema.backend.service.AuthService;
import com.nimbusds.jose.crypto.opts.UserAuthenticationRequired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    //@Autowired
    // private UserRepo userRepo;
    @Autowired
    private AuthService authService;
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody SignupRequest request){
        return ResponseEntity.ok(authService.register(request));
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {

        String token = authService.login(loginRequest);

        if (token == null) {
            return ResponseEntity.badRequest()
                    .body("Invalid email or password");
        }

        User user = authService.getUserByEmail(loginRequest.getEmail());

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("userId", user.getId());

        return ResponseEntity.ok(response);
    }
}
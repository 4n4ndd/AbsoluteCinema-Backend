package com.absolutecinema.backend.service;

import com.absolutecinema.backend.Repository.UserRepo;
import com.absolutecinema.backend.dto.LoginRequest;
import com.absolutecinema.backend.dto.SignupRequest;
import com.absolutecinema.backend.model.User;
import com.absolutecinema.backend.util.JWTutil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {

    @Autowired
    private OTPService otpService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String register(SignupRequest request) {
        if (userRepo.findByEmail(request.getEmail()).isPresent()) {
            return "Email already exists";
        }

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        user.setVerified(false);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        userRepo.save(user);

        String otp = otpService.generateOTP();

        otpService.storeOTP(request.getEmail(), otp);

        emailService.sendOTP(request.getEmail(), otp);

        return "OTP sent to your email";
    }

    public String login(LoginRequest loginRequest) {
        User user = userRepo.findByEmail(loginRequest.getEmail())
                .orElse(null);

        if (user == null) {
            return null;
        }

        if (!passwordEncoder.matches(
                loginRequest.getPassword(),
                user.getPassword()
        )) {
            return null;
        }

        if (!user.isVerified()) {
            return "Please verify your email first";
        }

        return JWTutil.generateToken(user.getEmail());
    }

    public String verifyOTP(String email, String enteredOtp) {
        boolean isValid = otpService.verifyOTP(email, enteredOtp);

        if (!isValid) {
            return "Invalid or expired OTP";
        }

        User user = userRepo.findByEmail(email)
                .orElse(null);

        if (user == null) {
            return "User not found";
        }

        user.setVerified(true);
        user.setUpdatedAt(LocalDateTime.now());

        userRepo.save(user);

        return "Email verified successfully";
    }

    public String resendOTP(String email) {
        User user = userRepo.findByEmail(email)
                .orElse(null);

        if (user == null) {
            return "User not found";
        }

        if (user.isVerified()) {
            return "User already verified";
        }

        String otp = otpService.generateOTP();

        otpService.storeOTP(email, otp);

        emailService.sendOTP(email, otp);

        return "New OTP sent successfully";
    }

    public String forgotPassword(String email) {
        User user = userRepo.findByEmail(email)
                .orElse(null);

        if (user == null) {
            return "User not found";
        }

        String otp = otpService.generateOTP();

        otpService.storeOTP(email, otp);

        emailService.sendOTP(email, otp);

        return "Password reset OTP sent";
    }

    public String verifyResetOTP(String email, String enteredOtp) {
        boolean isValid = otpService.verifyOTP(email, enteredOtp);

        if (!isValid) {
            return "Invalid or expired OTP";
        }

        return "OTP verified successfully";
    }

    public String resetPassword(String email, String newPassword) {
        User user = userRepo.findByEmail(email)
                .orElse(null);

        if (user == null) {
            return "User not found";
        }

        user.setPassword(
                passwordEncoder.encode(newPassword)
        );

        user.setUpdatedAt(LocalDateTime.now());

        userRepo.save(user);

        return "Password reset successful";
    }

    public User getUserByEmail(String email) {
        return userRepo.findByEmail(email)
                .orElse(null);
    }
}
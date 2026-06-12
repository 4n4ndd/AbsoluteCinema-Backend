package com.absolutecinema.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class OTPService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public String generateOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    public void storeOTP(String email, String otp) {
        redisTemplate.opsForValue()
                .set("OTP:" + email, otp, 5, TimeUnit.MINUTES);
    }

    public String getOTP(String email) {
        return redisTemplate.opsForValue()
                .get("OTP:" + email);
    }

    public boolean verifyOTP(String email, String enteredOtp) {
        String storedOtp = getOTP(email);

        if (storedOtp == null) {
            return false;
        }

        if (!storedOtp.equals(enteredOtp)) {
            return false;
        }

        redisTemplate.delete("OTP:" + email);

        return true;
    }
}
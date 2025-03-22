package com.rcs.regulatoryComplianceSystem.service;

import com.rcs.regulatoryComplianceSystem.entity.User;
import com.rcs.regulatoryComplianceSystem.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Ensure this is a BCryptPasswordEncoder

    // Generate Reset Token
    public String generateResetToken(String email) {
        log.info("Received password reset request for email: {}", email);

        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            log.warn("Password reset request failed: No user found with email {}", email);
            return "User not found";
        }

        User user = optionalUser.get();
        String token = UUID.randomUUID().toString(); // Generate unique token
        Date expiryDate = new Date(System.currentTimeMillis() + 1000 * 60 * 15); // 15 min expiry

        user.setResetToken(token);
        user.setResetTokenExpiry(expiryDate);
        userRepository.save(user);

        log.info("Generated reset token for user {}: {} (Expires at: {})", user.getEmail(), token, expiryDate);
        return  token;
    }

    // Reset Password
    public String resetPassword(String token, String newPassword) {
        log.info("Received password reset attempt for token: {}", token);

        Optional<User> optionalUser = userRepository.findByResetToken(token);
        log.info("user",optionalUser);
        if (optionalUser.isEmpty()) {
            log.warn("Password reset failed: Invalid token {}", token);
            return "Invalid or expired token";
        }

        User user = optionalUser.get();

        // Check token expiry
        if (user.getResetTokenExpiry() == null || user.getResetTokenExpiry().before(new Date())) {
            log.warn("Password reset failed: Token expired for user {}", user.getEmail());
            return "Invalid or expired token";
        }

        // Reset password
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
        userRepository.save(user);

        log.info("Password reset successfully for user: {}", user.getEmail());
        return "Password reset successfully";
    }
}

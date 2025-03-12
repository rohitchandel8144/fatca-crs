package com.rcs.regulatoryComplianceSystem.controller;

import com.rcs.regulatoryComplianceSystem.DTO.AuthDTO.AuthRequest;
import com.rcs.regulatoryComplianceSystem.DTO.AuthDTO.AuthResponse;
import com.rcs.regulatoryComplianceSystem.DTO.AuthDTO.RegisterRequest;
import com.rcs.regulatoryComplianceSystem.entity.Role;
import com.rcs.regulatoryComplianceSystem.entity.User;
import com.rcs.regulatoryComplianceSystem.repositories.RoleRepository;
import com.rcs.regulatoryComplianceSystem.repositories.UserRepository;
import com.rcs.regulatoryComplianceSystem.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {

        System.out.println("request reached contoller");
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );


            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            String token = jwtUtil.generateToken(user);

            return ResponseEntity.ok(new AuthResponse(
                    "Login successful",
                    true,
                    token,
                    user.getUserId(),
                    user.getInstitution(),
                    user.getRole().getRoleName().name(),
                    user.getUsername(),
                    user.getEmail()
                    ));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse(
                            "Invalid email or password",
                            false,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null
                            ));
        }
    }


    @PostMapping("/register")
    public ResponseEntity<AuthResponse>register(@RequestBody RegisterRequest request) {
        // Check if email already exists
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED)
                    .body(new AuthResponse(
                            "Email already exists!",
                            false,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null
                            ));
        }

        Role userRole = roleRepository.findByRoleName(Role.RoleName.MAKER)
                .orElseThrow(() -> new RuntimeException("Default Role Not Found"));

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(userRole);
        userRepository.save(user);
        String token = jwtUtil.generateToken(user);

        return  ResponseEntity.ok(new AuthResponse(
                "User registered successfully!",
                true,
                token,
                user.getUserId(),
                user.getInstitution(),
                user.getRole().getRoleName().name(),
                user.getUsername(),
                user.getEmail()
                ));
    }
}

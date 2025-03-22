package com.rcs.regulatoryComplianceSystem.controller;

import com.rcs.regulatoryComplianceSystem.DTO.AuthDTO.AuthRequest;
import com.rcs.regulatoryComplianceSystem.DTO.AuthDTO.AuthResponse;
import com.rcs.regulatoryComplianceSystem.DTO.AuthDTO.RegisterRequest;
import com.rcs.regulatoryComplianceSystem.DTO.UserLoginDTO;
import com.rcs.regulatoryComplianceSystem.entity.*;
import com.rcs.regulatoryComplianceSystem.repositories.PermissionRepository;
import com.rcs.regulatoryComplianceSystem.repositories.RoleRepository;
import com.rcs.regulatoryComplianceSystem.repositories.UserPermissionRepository;
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

import javax.management.relation.RoleNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

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
    private PermissionRepository permissionRepository;

    @Autowired
    private UserPermissionRepository userPermissionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            UserLoginDTO userLoginDTO = userRepository.findUserDetailsByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));


            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // ✅ Convert Set<Role> to List<String>
            List<String> roles = user.getRoles().stream()
                    .map(role -> role.getRoleName().name())
                    .collect(Collectors.toList());

            // ✅ Convert Set<UserPermission> to List<String>
            List<String> permissions = userPermissionRepository.findByUser(user).stream()
                    .map(up -> up.getPermission().getName().name())
                    .collect(Collectors.toList());

            // Generate JWT with roles and permissions
            String token = jwtUtil.generateToken(user, roles, permissions);

            return ResponseEntity.ok(new AuthResponse(
                    "Login successful",
                    true,
                    token,
                    userLoginDTO.getUserId(),
                    userLoginDTO.getInstitutionId(),
                    roles,
                    userLoginDTO.getName(),
                    userLoginDTO.getEmail(),
                    permissions
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
                            null,
                            null
                    ));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) throws RoleNotFoundException {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new AuthResponse(
                            "Email already exists!",
                            false,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            new ArrayList<>()
                    ));
        }

        // Assign roles
        Set<Role> assignedRoles = new HashSet<>();

        if (request.getRoles() != null && !request.getRoles().isEmpty()) {
            for (String roleName : request.getRoles()) {
                try {
                    Role role = roleRepository.findByRoleName(Role.RoleName.valueOf(roleName.toUpperCase()))
                            .orElseThrow(() -> new RoleNotFoundException("Role Not Found: " + roleName));
                    assignedRoles.add(role);
                } catch (IllegalArgumentException | RoleNotFoundException e) {
                    throw new RoleNotFoundException("Invalid Role Name: " + roleName);
                }
            }
        } else {
            if ("RFI".equals(request.getRoleType())) {
                Role defaultRole = roleRepository.findByRoleName(Role.RoleName.MAKER)
                        .orElseThrow(() -> new RoleNotFoundException("Default Role Not Found"));
                assignedRoles.add(defaultRole);
            }
            if ("MINISTRY".equals(request.getRoleType())) {
                Role defaultRole = roleRepository.findByRoleName(Role.RoleName.OFFICE_BEARER)
                        .orElseThrow(() -> new RoleNotFoundException("Default Role Not Found"));
                assignedRoles.add(defaultRole);
            }
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(assignedRoles);

        user = userRepository.save(user);

        // Assign permissions
        List<UserPermission> userPermissions = new ArrayList<>();
        List<String> assignedPermissions = new ArrayList<>();

        // ✅ Proper conversion of permissions
        List<String> permissionsToAssign = (request.getPermissions() == null || request.getPermissions().isEmpty())
                ? List.of("READ") // Default to READ permission
                : new ArrayList<>(request.getPermissions());

        for (String permissionName : permissionsToAssign) {
            Permission permission = permissionRepository.findByName(Permission.PermissionType.valueOf(permissionName))
                    .orElseThrow(() -> new RuntimeException("Permission Not Found: " + permissionName));

            UserPermission userPermission = new UserPermission();
            userPermission.setUser(user);
            userPermission.setPermission(permission);
            userPermissions.add(userPermission);
            assignedPermissions.add(permissionName);
        }

        userPermissionRepository.saveAll(userPermissions);

        // ✅ Convert Set<Role> to List<String> correctly
        List<String> roles = assignedRoles.stream()
                .map(role -> role.getRoleName().name())
                .collect(Collectors.toList());

        // Generate token with roles and permissions
        String token = jwtUtil.generateToken(user, roles, assignedPermissions);

        return ResponseEntity.ok(new AuthResponse(
                "User registered successfully!",
                true,
                token,
                user.getUserId(),
                (user.getInstitution() != null && user.getInstitution().getInstitutionId() != null)
                        ? user.getInstitution().getInstitutionId()
                        : null,
                roles,
                user.getUsername(),
                user.getEmail(),
                assignedPermissions
        ));
    }
}

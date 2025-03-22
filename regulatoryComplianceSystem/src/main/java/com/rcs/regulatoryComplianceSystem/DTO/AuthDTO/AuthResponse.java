package com.rcs.regulatoryComplianceSystem.DTO.AuthDTO;

import com.rcs.regulatoryComplianceSystem.entity.Institution;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String message;
    private boolean success;
    private String token;
    private Long id;
    private Long institution;
    private List<String> roles;  // Updated to handle multiple roles
    private String name;
    private String email;
    private List<String> permissions;  // Added to return user-specific permissions
}

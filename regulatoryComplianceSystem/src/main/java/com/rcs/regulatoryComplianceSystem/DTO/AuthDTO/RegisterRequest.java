package com.rcs.regulatoryComplianceSystem.DTO.AuthDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private Set<String> roles;
    private String roleType;
    private Set<String> permissions; // Optional: U
}

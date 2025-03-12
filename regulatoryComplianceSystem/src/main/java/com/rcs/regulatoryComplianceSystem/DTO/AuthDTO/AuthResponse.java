package com.rcs.regulatoryComplianceSystem.DTO.AuthDTO;

import com.rcs.regulatoryComplianceSystem.entity.Institution;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String message;
    private boolean success;
    private String token;
    private Long id;
    private Institution institution;
    private String role;
    private String name;
    private String email;
}

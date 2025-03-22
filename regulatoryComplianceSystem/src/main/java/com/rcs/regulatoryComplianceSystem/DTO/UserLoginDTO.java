package com.rcs.regulatoryComplianceSystem.DTO;

import com.rcs.regulatoryComplianceSystem.entity.Role;
import java.util.Set;

public class UserLoginDTO {
    private Long userId;
    private String name;
    private String email;
    private Long institutionId;

    public UserLoginDTO(Long userId, String name, String email, Long institutionId) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.institutionId = institutionId;

    }

    // Getters
    public Long getUserId() { return userId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public Long getInstitutionId() { return institutionId; }
}

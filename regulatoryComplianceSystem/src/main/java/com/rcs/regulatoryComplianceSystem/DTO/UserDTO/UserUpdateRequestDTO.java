package com.rcs.regulatoryComplianceSystem.DTO.UserDTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter

public class UserUpdateRequestDTO {
    private List<String> roles;
    private List<String> permissions;

    // Getters and Setters
    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }
}

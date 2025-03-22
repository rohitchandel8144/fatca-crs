package com.rcs.regulatoryComplianceSystem.DTO.UserDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordRequest {
    private String token;
    private String newPassword;
}

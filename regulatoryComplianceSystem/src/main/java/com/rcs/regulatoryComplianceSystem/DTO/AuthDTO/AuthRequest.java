package com.rcs.regulatoryComplianceSystem.DTO.AuthDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRequest {

    private String email;
    private String password;
}

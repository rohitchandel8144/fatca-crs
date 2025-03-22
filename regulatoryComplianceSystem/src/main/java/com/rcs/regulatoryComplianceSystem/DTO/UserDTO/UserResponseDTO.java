package com.rcs.regulatoryComplianceSystem.DTO.UserDTO;

import com.rcs.regulatoryComplianceSystem.DTO.InstitutionDTO.InstitutionResponseDTO;
import com.rcs.regulatoryComplianceSystem.entity.Institution;
import com.rcs.regulatoryComplianceSystem.entity.Role;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
    private Long userId;
    private String name;
    private String email;
    private List<String> roles;
    private InstitutionResponseDTO institution;
    private Date createdAt;
    private List<String> permissions;
}

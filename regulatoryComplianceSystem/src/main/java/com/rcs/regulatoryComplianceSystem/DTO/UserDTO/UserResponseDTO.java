package com.rcs.regulatoryComplianceSystem.DTO.UserDTO;

import com.rcs.regulatoryComplianceSystem.entity.Institution;
import com.rcs.regulatoryComplianceSystem.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
    private Long userId;
    private String name;
    private String email;
    private Role role;
    private Institution institution;
    private Date createdAt;

}

package com.rcs.regulatoryComplianceSystem.DTO;

import com.rcs.regulatoryComplianceSystem.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RolesAndPermissionsResponse {

    private List<Role> ministryRoles;
    private List<String> allPermissions;


}

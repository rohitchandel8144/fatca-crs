package com.rcs.regulatoryComplianceSystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "roles")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Role {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;

    @Enumerated(EnumType.STRING)
    private RoleName roleName;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    public enum RoleName { SUPERADMIN, SUBADMIN, IT_ADMIN, OFFICE_BEARER, ADMINISTRATOR, RFI_SUBADMIN, MAKER, CHECKER }
     public enum RoleType { MINISTRY, RFI }
}
package com.rcs.regulatoryComplianceSystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "permissions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Permission {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PermissionType name;

    private String description;

    public enum PermissionType {
        READ,
        WRITE,
        UPDATE ,
        DELETE,
        APPROVE,
        REJECT ,
        REVIEW
    }
}

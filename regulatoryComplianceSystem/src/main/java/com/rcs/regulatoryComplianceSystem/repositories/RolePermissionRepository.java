package com.rcs.regulatoryComplianceSystem.repositories;


import com.rcs.regulatoryComplianceSystem.entity.RolePermission;
import com.rcs.regulatoryComplianceSystem.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {
    List<RolePermission> findByRole(Role role);
}

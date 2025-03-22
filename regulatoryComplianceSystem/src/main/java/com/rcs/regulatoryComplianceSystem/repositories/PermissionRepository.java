package com.rcs.regulatoryComplianceSystem.repositories;


import com.rcs.regulatoryComplianceSystem.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Optional<Permission> findByName(Permission.PermissionType name);
}

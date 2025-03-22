package com.rcs.regulatoryComplianceSystem.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.rcs.regulatoryComplianceSystem.entity.Role;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByRoleName(Role.RoleName roleName);
    List<Role> findByRoleType(Role.RoleType roleType);

}

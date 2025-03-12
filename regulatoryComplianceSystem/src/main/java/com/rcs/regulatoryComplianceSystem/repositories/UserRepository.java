package com.rcs.regulatoryComplianceSystem.repositories;

import com.rcs.regulatoryComplianceSystem.entity.Role;
import com.rcs.regulatoryComplianceSystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
    List<User> findByRole_RoleType(Role.RoleType roleType);


}

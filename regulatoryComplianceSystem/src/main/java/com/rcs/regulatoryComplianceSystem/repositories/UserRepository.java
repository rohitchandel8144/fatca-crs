package com.rcs.regulatoryComplianceSystem.repositories;

import com.rcs.regulatoryComplianceSystem.DTO.UserLoginDTO;
import com.rcs.regulatoryComplianceSystem.entity.Institution;
import com.rcs.regulatoryComplianceSystem.entity.Role;
import com.rcs.regulatoryComplianceSystem.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
    List<User> findByRoles_RoleType(Role.RoleType roleType);
    Optional<User> findByResetToken(String resetToken);
    @Query("""
        SELECT new com.rcs.regulatoryComplianceSystem.DTO.UserLoginDTO(
            u.userId, u.name, u.email, u.institution.institutionId
        ) 
        FROM User u 
        WHERE u.email = :email
    """)
    Optional<UserLoginDTO> findUserDetailsByEmail(@Param("email") String email);
    List<User> findByInstitution(Institution institution);


}

package com.rcs.regulatoryComplianceSystem.repositories;



import com.rcs.regulatoryComplianceSystem.entity.UserPermission;
import com.rcs.regulatoryComplianceSystem.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserPermissionRepository extends JpaRepository<UserPermission, Long> {
    List<UserPermission> findByUser(User user);

        @Modifying
        @Transactional
        @Query("DELETE FROM UserPermission up WHERE up.user = :user")
        void deleteByUser(@Param("user") User user);


}

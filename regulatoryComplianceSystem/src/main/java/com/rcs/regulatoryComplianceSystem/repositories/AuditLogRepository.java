package com.rcs.regulatoryComplianceSystem.repositories;

import com.rcs.regulatoryComplianceSystem.entity.AuditLog;
import com.rcs.regulatoryComplianceSystem.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog,Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM AuditLog a WHERE a.user.userId = :userId")
    void deleteByUserId(@Param("userId") Long userId);

}

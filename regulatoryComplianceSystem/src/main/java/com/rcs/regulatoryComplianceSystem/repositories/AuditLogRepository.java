package com.rcs.regulatoryComplianceSystem.repositories;

import com.rcs.regulatoryComplianceSystem.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog,Long> {

}

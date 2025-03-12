package com.rcs.regulatoryComplianceSystem.service;

import com.rcs.regulatoryComplianceSystem.entity.AuditLog;
import com.rcs.regulatoryComplianceSystem.entity.User;
import com.rcs.regulatoryComplianceSystem.repositories.AuditLogRepository;
import com.rcs.regulatoryComplianceSystem.repositories.UserRepository;
import com.rcs.regulatoryComplianceSystem.service.serviceImp.AuditLogImp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuditLogService implements AuditLogImp {

    private static final Logger logger = LoggerFactory.getLogger(AuditLogService.class);

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void logAction(String email, String action) {
        try {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(()-> new UsernameNotFoundException("email not found"));
            AuditLog log = new AuditLog(user, action);
            auditLogRepository.save(log);
            logger.info("Audit log saved: User {} performed {}", user.getEmail(), action);
        } catch (Exception e) {
            logger.error("Error saving audit log for user {}: {}",email, e);
        }
    }
}

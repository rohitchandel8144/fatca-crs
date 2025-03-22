package com.rcs.regulatoryComplianceSystem.service;

import com.rcs.regulatoryComplianceSystem.DTO.AuditLogDto;
import com.rcs.regulatoryComplianceSystem.entity.AuditLog;
import com.rcs.regulatoryComplianceSystem.entity.Role;
import com.rcs.regulatoryComplianceSystem.entity.User;
import com.rcs.regulatoryComplianceSystem.repositories.AuditLogRepository;
import com.rcs.regulatoryComplianceSystem.repositories.UserRepository;
import com.rcs.regulatoryComplianceSystem.service.serviceImp.AuditLogImp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

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


    public List<AuditLogDto> getLogs(String value) {
        List<AuditLog> auditLogs = auditLogRepository.findAll(); // Fetch all logs once

        Role.RoleType roleType = Objects.equals(value, "MINISTRY") ? Role.RoleType.MINISTRY : Role.RoleType.RFI;

        return auditLogs.stream()
                .filter(auditLog -> auditLog.getUser().getRoles().stream()
                        .anyMatch(role -> role.getRoleType() == roleType))
                .map(filteredLog -> new AuditLogDto(
                        filteredLog.getLogId(),
                        filteredLog.getUser().getUserId(),
                        filteredLog.getAction(),
                        filteredLog.getTimestamp()
                ))
                .toList();
    }


}

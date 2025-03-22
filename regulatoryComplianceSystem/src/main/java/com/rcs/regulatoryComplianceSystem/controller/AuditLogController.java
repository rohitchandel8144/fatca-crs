package com.rcs.regulatoryComplianceSystem.controller;

import com.rcs.regulatoryComplianceSystem.DTO.AuditLogDto;
import com.rcs.regulatoryComplianceSystem.entity.AuditLog;
import com.rcs.regulatoryComplianceSystem.service.AuditLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/audit-logs")
public class AuditLogController {

    @Autowired
    private AuditLogService auditLogService;

    @PreAuthorize("hasRole('IT_ADMIN')")
    @GetMapping("/get-logs/{value}")
    public ResponseEntity<List<AuditLogDto>> getLogs(@PathVariable String value){
        List<AuditLogDto> auditLogDtos = auditLogService.getLogs(value);
        return ResponseEntity.ok(auditLogDtos);
    }

}

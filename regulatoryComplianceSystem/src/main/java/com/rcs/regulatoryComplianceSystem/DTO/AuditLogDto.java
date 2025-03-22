package com.rcs.regulatoryComplianceSystem.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditLogDto {
    private Long logId;
    private Long userId;
    private String action;
    private Date timestamp;
}


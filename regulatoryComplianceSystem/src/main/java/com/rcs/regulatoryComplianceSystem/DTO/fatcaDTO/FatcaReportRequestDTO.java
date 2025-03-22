package com.rcs.regulatoryComplianceSystem.DTO.fatcaDTO;

import com.rcs.regulatoryComplianceSystem.entity.fatcaEntity.ReportingFI;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FatcaReportRequestDTO {
    private ReportingFI reportingFI;
    private MultipartFile file;
    private String createdByUserId;
}


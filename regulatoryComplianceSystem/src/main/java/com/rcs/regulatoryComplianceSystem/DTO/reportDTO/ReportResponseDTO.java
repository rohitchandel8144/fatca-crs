package com.rcs.regulatoryComplianceSystem.DTO.reportDTO;

import com.rcs.regulatoryComplianceSystem.DTO.InstitutionDTO.InstitutionResponseDTO;
import com.rcs.regulatoryComplianceSystem.entity.Institution;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReportResponseDTO {
    private Long reportId;
    private String status;
    private String reportType;
    private Long institutionId;
    private String institutionClassification;
    private String regulatoryAuthority;
    private String registerDates;

}

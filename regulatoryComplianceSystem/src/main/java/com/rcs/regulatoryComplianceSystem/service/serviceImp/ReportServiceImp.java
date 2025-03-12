package com.rcs.regulatoryComplianceSystem.service.serviceImp;

import com.rcs.regulatoryComplianceSystem.DTO.reportDTO.ReportResponseDTO;
import com.rcs.regulatoryComplianceSystem.entity.Report;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ReportServiceImp {
    public String uploadReport(Long institutionId, Long uploadedById, String reportType, String registrationDate, MultipartFile file, String institutionClassification, String regulatoryAuthority);
    //public List<Report> getAllPendingRFIReports();
    public String approveReport(Long userId,Long reportId);
    public String rejectReport(Long userId,Long reportId,String msg);

    public String approveReportMinistry(Long reportID,Long approvedByUserId);
    public String rejectReportMinistry(Long userId, Long reportId, String reason);
    public List<Report> getAllPendingReports();
    public List<Report> getAllReports();

    public List<ReportResponseDTO> getAllPendingRFIReports() ;



}

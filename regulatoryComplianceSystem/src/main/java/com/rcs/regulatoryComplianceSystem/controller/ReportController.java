package com.rcs.regulatoryComplianceSystem.controller;

import com.rcs.regulatoryComplianceSystem.DTO.reportDTO.ReportResponseDTO;
import com.rcs.regulatoryComplianceSystem.entity.Report;
import com.rcs.regulatoryComplianceSystem.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @PreAuthorize("hasRole('MAKER')")
    @PostMapping("/upload")
    public ResponseEntity<String> uploadReport(
            @RequestParam Long institutionId,
            @RequestParam Long uploadedById,
            @RequestParam String reportType,
            @RequestParam String registrationDate,
            @RequestParam MultipartFile file,
            @RequestParam String institutionClassification,
            @RequestParam String regulatoryAuthority
    ) {
        reportService.uploadReport(institutionId, uploadedById, reportType, registrationDate, file, institutionClassification, regulatoryAuthority);
        return ResponseEntity.ok("Report uploaded successfully and pending approval.");
    }


    @PreAuthorize("hasRole('SUPERADMIN') or hasRole('SUBADMIN')")
    @GetMapping("/get-all-rfi-reports")
    public ResponseEntity<List<ReportResponseDTO>> getAllRFIReports(){
        List<ReportResponseDTO> reports = reportService.getAllPendingRFIReports();
        return  ResponseEntity.ok(reports);
    }


    @PreAuthorize("hasAnyRole('ADMINISTRATOR','RFI_SUBADMIN','CHECKER')")
    @PostMapping("/approve-report/{userId}/{reportId}")
    public ResponseEntity<String> approveReport(@PathVariable Long userId,@PathVariable Long reportId){
        String msg = reportService.approveReport(userId,reportId);
        return  ResponseEntity.ok(msg);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRATOR','RFI_SUBADMIN','CHECKER')")
    @PostMapping("/reject-report/{userId}/{reportId}")
    public ResponseEntity<String> rejectReport(@PathVariable Long userId,@PathVariable Long reportId,@RequestBody String reason){
        String msg = reportService.rejectReport(userId,reportId,reason);
        return  ResponseEntity.ok(msg);
    }


    @PreAuthorize("hasRole('SUPERADMIN') or hasRole('SUBADMIN')")
    @PostMapping("/approve-report-ministry/{reportId}/{approvedByUserId}")
    public ResponseEntity<String> approveReportMinistry(@PathVariable Long reportId,@PathVariable Long approvedByUserId){
        String msg = reportService.approveReportMinistry(reportId,approvedByUserId);
        return ResponseEntity.ok(msg);
    }

    @PreAuthorize("hasRole('SUPERADMIN') or hasRole('SUBADMIN')")
    @PostMapping("/reject-report-ministry/{reportId}/{approvedByUserId}")
    public ResponseEntity<String> rejectReportMinistry(@PathVariable Long reportId,@PathVariable Long approvedByUserId,@RequestBody String reason){
        String msg = reportService.rejectReportMinistry(reportId,approvedByUserId,reason);
        return ResponseEntity.ok(msg);
    }

    @PreAuthorize("hasRole('SUPERADMIN') or hasRole('SUBADMIN')")
    @GetMapping("/get-all-pending-reports")
    public ResponseEntity<List<ReportResponseDTO>> getAllPendingReports(){
        List<ReportResponseDTO> reports = reportService.getAllPendingReports();
        return ResponseEntity.ok(reports);
    }


    @PreAuthorize("hasAnyRole('ADMINISTRATOR','RFI_SUBADMIN','CHECKER')")
    @GetMapping("/get-all-reports")
    public ResponseEntity<List<ReportResponseDTO>> getAllreports(){
        List<ReportResponseDTO> reports = reportService.getAllReports();
        return ResponseEntity.ok(reports);
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @DeleteMapping("/delete/{reportId}")
    public  ResponseEntity<String > deleteReport(@PathVariable Long reportId){
        String msg= reportService.deleteReport(reportId);
        return ResponseEntity.ok(msg);

     }

    }


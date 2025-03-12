package com.rcs.regulatoryComplianceSystem.service;

import com.rcs.regulatoryComplianceSystem.DTO.reportDTO.ReportResponseDTO;
import com.rcs.regulatoryComplianceSystem.entity.Institution;
import com.rcs.regulatoryComplianceSystem.entity.Notification;
import com.rcs.regulatoryComplianceSystem.entity.Report;
import com.rcs.regulatoryComplianceSystem.entity.User;
import com.rcs.regulatoryComplianceSystem.repositories.InstitutionRepository;
import com.rcs.regulatoryComplianceSystem.repositories.NotificationRepository;
import com.rcs.regulatoryComplianceSystem.repositories.ReportRepository;
import com.rcs.regulatoryComplianceSystem.repositories.UserRepository;
import com.rcs.regulatoryComplianceSystem.service.serviceImp.ReportServiceImp;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


@Service
public class ReportService implements ReportServiceImp {
    @Autowired
    private InstitutionRepository institutionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ReportRepository  reportRepository;
    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public String uploadReport(Long institutionId, Long uploadedById, String reportType, String registrationDates, MultipartFile file,String institutionClassification,String regulatoryAuthority) {
        Institution institution = institutionRepository.findById(institutionId)
                .orElseThrow(() -> new RuntimeException("Institution not found"));

        User user = userRepository.findById(uploadedById)
                .orElseThrow(() -> new RuntimeException("User not found"));

        try {
            Report report = new Report();
            report.setInstitution(institution);
            report.setUploadedBy(user);
            report.setReportType(reportType);
            report.setInstitutionClassification(institutionClassification);
            report.setRegistrationDate(registrationDates);
            report.setStatus(Report.Status.PENDING);
            report.setFileName(file.getOriginalFilename());
            report.setRegulatoryAuthority(regulatoryAuthority);
            report.setFileType(file.getContentType());

            if (!file.getContentType().equals("application/xml") && !file.getContentType().equals("text/xml")) {
                throw new RuntimeException("Invalid file type. Only XML files are allowed.");
            }

            String xmlContent = new String(file.getBytes(), StandardCharsets.UTF_8);

            if (xmlContent.isEmpty()) {
                throw new RuntimeException("Uploaded XML file is empty");
            }

            report.setFileData(xmlContent);
            reportRepository.save(report);
            return "Report created successfully";

        } catch (IOException e) {
            throw new RuntimeException("Error saving XML file data: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public List<ReportResponseDTO> getAllPendingRFIReports() {
        List<Report.Status> statuses = Arrays.asList(
                Report.Status.APPROVEDBYRFI,
                Report.Status.APPROVEDBYMINISTRY,
                Report.Status.REJECTEDBYMINISTRY
        );

        List<Report> reports = reportRepository.findByStatusIn(statuses);

        List<ReportResponseDTO> responseList = reports.stream().map(report -> new ReportResponseDTO(
                report.getReportId(),
                report.getStatus().toString(),
                report.getReportType(),
                report.getInstitution(),
                report.getInstitutionClassification(),
                report.getRegulatoryAuthority(),
                report.getRegistrationDate()
        )).toList();

        return responseList;
    }


    @Override
    public String approveReport(Long userId, Long reportId) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!report.getInstitution().getInstitutionId().equals(user.getInstitution().getInstitutionId())) {
            throw new RuntimeException("User not authorized to approve this report");
        }

        report.setStatus(Report.Status.APPROVEDBYRFI);
        report.setApprovedBy(user);
        report.setApprovedAt(new Date());

        reportRepository.save(report);

        Notification notification = new Notification();
        notification.setMessage("Report " + report.getReportType() + " for " + report.getInstitution().getName() + " has been approved.");
        notification.setStatus(Notification.Status.UNREAD);
        notification.setRecipientPanel("MINISTRY");
        notification.setCreatedAt(new Date());
        // Save the notification
        notificationRepository.save(notification);
        return "Report approved successfully";
    }

    @Override
    public String rejectReport(Long userId,Long reportId,String reason) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!report.getInstitution().getInstitutionId().equals(user.getInstitution().getInstitutionId())) {
            throw new RuntimeException("User not authorized to approve this report");
        }

        report.setStatus(Report.Status.REJECTEDBYRFI);
        report.setRejectedBy(user);
        report.setRejectedAt(new Date());
        report.setRejectionReason(reason);
        report.setApprovedAt(new Date());

        reportRepository.save(report);
        Notification notification = new Notification();
        notification.setMessage("Report " + report.getReportType() + " for " + report.getInstitution().getName() + " has been rejected.");
        notification.setStatus(Notification.Status.UNREAD);
        notification.setRecipientPanel("MINISTRY");
        notification.setCreatedAt(new Date());
        notificationRepository.save(notification);

        return "Report rejected ";
    }

    @Override
    public String approveReportMinistry(Long reportID, Long approvedByUserId) {

        User user = userRepository.findById(approvedByUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Report report = reportRepository.findById(reportID)
                .orElseThrow(() -> new RuntimeException("Report not found"));


        if (report.getStatus() != Report.Status.APPROVEDBYRFI) {
            throw new RuntimeException("Report must be approved by RFI first.");
        }

        report.setStatus(Report.Status.APPROVEDBYMINISTRY);
        report.setApprovedBy(user);
        report.setApprovedAt(new Date());
        reportRepository.save(report);

        Notification notification = new Notification();
        notification.setMessage("Report ID " + reportID + " has been approved by Ministry.");
        notification.setStatus(Notification.Status.UNREAD);
        notification.setRecipientPanel("RFI");
        notification.setCreatedAt(new Date());
        notificationRepository.save(notification);
        return "Report successfully approved by Ministry.";
    }

    @Override
    public String rejectReportMinistry( Long reportId,Long userId, String reason) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (report.getStatus() != Report.Status.APPROVEDBYRFI) {
            throw new RuntimeException("Report must be approved by RFI first.");
        }

        report.setStatus(Report.Status.REJECTEDBYMINISTRY);
        report.setRejectedBy(user);
        report.setRejectionReason(reason);
        report.setApprovedAt(new Date());
        report.setRejectionReason(reason);
        reportRepository.save(report);

        Notification notification = new Notification();
        notification.setMessage("Report ID " + reportId + " has been rejected by Ministry. Reason: " + reason);
        notification.setStatus(Notification.Status.UNREAD);
        notification.setRecipientPanel("RFI");
        notification.setCreatedAt(new Date());
        notificationRepository.save(notification);
        return "Report rejected successfully";
    }

    @Override
    @Transactional
    public List<Report> getAllPendingReports() {
        return  reportRepository.findByStatus(Report.Status.APPROVEDBYRFI);
    }

    @Override
    public List<Report> getAllReports() {
        List<Report> reports = reportRepository.findAll();
        return reports;
    }


}

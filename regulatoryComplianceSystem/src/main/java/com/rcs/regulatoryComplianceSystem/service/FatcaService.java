package com.rcs.regulatoryComplianceSystem.service;

import com.rcs.regulatoryComplianceSystem.DTO.fatcaDTO.FatcaReportRequestDTO;
import com.rcs.regulatoryComplianceSystem.entity.Institution;
import com.rcs.regulatoryComplianceSystem.entity.User;
import com.rcs.regulatoryComplianceSystem.entity.fatcaEntity.*;
import com.rcs.regulatoryComplianceSystem.repositories.FatcaReportRepository;
import com.rcs.regulatoryComplianceSystem.repositories.MessageRepository;
import com.rcs.regulatoryComplianceSystem.repositories.UserRepository;
import com.rcs.regulatoryComplianceSystem.util.FileProcessors.CsvFatcaProcessor;
import com.rcs.regulatoryComplianceSystem.util.FileProcessors.ExcelFatcaProcessor;
import com.rcs.regulatoryComplianceSystem.util.FileProcessors.PdfFatcaProcessor;
import com.rcs.regulatoryComplianceSystem.util.KeyLoader;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.security.PrivateKey;
import java.security.PublicKey;

import java.util.List;

@Service
public class FatcaService {

    @Autowired
    private FatcaReportRepository fatcaReportRepository;

    @Autowired
    private FatcaXmlGenerator fatcaXmlGenerator;

    @Autowired
    private CsvFatcaProcessor csvFatcaProcessor;

    @Autowired
    private PdfFatcaProcessor pdfFatcaProcessor;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private KeyLoader keyLoader;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public String processFileAndSave(MultipartFile file, FatcaReportRequestDTO fatcaReportRequestDTO,Long userId) throws Exception {
        User createdBy = userRepository.findById(userId)
                .orElseThrow(()-> new UsernameNotFoundException("user not found"));
        Institution institution = createdBy.getInstitution();

        if (institution == null) {
            throw new RuntimeException("User is not associated with any institution.");
        }

        ReportingFI reportingFI = new ReportingFI();
        reportingFI.setAddress(fatcaReportRequestDTO.getReportingFI().getAddress());
        reportingFI.setName(fatcaReportRequestDTO.getReportingFI().getName());
        reportingFI.setTin(fatcaReportRequestDTO.getReportingFI().getTin());
        reportingFI.setResCountryCode(fatcaReportRequestDTO.getReportingFI().getResCountryCode());

        List<AccountReport> accounts = extractDataFromFile(file);

        if (accounts.isEmpty()) {
            throw new RuntimeException("File is empty or contains no valid FATCA data.");
        }

        MessageSpec messageSpec = new MessageSpec();
        messageSpec.setMessageRefId("MSG-" + System.currentTimeMillis());
        messageSpec = messageRepository.save(messageSpec);

        FatcaReport fatcaReport = new FatcaReport();
        fatcaReport.setMessageSpec(messageSpec);
        fatcaReport.setReportingFI(reportingFI);

        ReportingGroup reportingGroup = new ReportingGroup();
        reportingGroup.setFatcaReport(fatcaReport);

        for (AccountReport account : accounts) {
            account.setReportingGroup(reportingGroup);
        }

        reportingGroup.setAccountReports(accounts);
        fatcaReport.setReportingGroups(List.of(reportingGroup));
        fatcaReport.setInstitution(institution);
        fatcaReport.setCreatedBy(createdBy);
        fatcaReport.setStatus(FatcaReport.ReportStatus.PENDING);
        String xmlData = fatcaXmlGenerator.generateFatcaXml(fatcaReport);
        fatcaReport.setXmlData(xmlData);

        fatcaReportRepository.save(fatcaReport);
        return "FATCA report successfully registered.";
    }
    private List<AccountReport> extractDataFromFile(MultipartFile file) throws Exception {
        String fileName = file.getOriginalFilename();
        if (fileName.endsWith(".csv")) {
            return CsvFatcaProcessor.processCSV(file);
        } else if (fileName.endsWith(".xlsx")) {
            return ExcelFatcaProcessor.processExcel(file);
        } else if (fileName.endsWith(".pdf")) {
            return PdfFatcaProcessor.processPDF(file);
        } else {
            throw new IllegalArgumentException("Unsupported file format.");
        }
    }
}

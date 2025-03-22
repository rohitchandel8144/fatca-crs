package com.rcs.regulatoryComplianceSystem.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.rcs.regulatoryComplianceSystem.DTO.InstitutionDTO.InstitutionRequestDTO;
import com.rcs.regulatoryComplianceSystem.DTO.fatcaDTO.FatcaReportRequestDTO;
import com.rcs.regulatoryComplianceSystem.DTO.reportDTO.ReportResponseDTO;
import com.rcs.regulatoryComplianceSystem.entity.fatcaEntity.ReportingFI;
import com.rcs.regulatoryComplianceSystem.service.FatcaService;
import jakarta.xml.bind.JAXBException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/fatca")
public class FatcaController {

    @Autowired
    private FatcaService fatcaService;

    @PostMapping(value = "/register",consumes = "multipart/form-data")
    public ResponseEntity<String> registerFatcaReport(@RequestParam("reportingFI") String reportingFI,
                                                      @RequestParam("file") MultipartFile file,
                                                      @RequestParam("createdByUserId") String createdByUserID ) throws Exception {
         Long userID = Long.valueOf(createdByUserID);
        ObjectMapper objectMapper = new ObjectMapper();
       FatcaReportRequestDTO requestDTO = objectMapper.readValue(reportingFI,FatcaReportRequestDTO.class);
        String msg=fatcaService.processFileAndSave(file,requestDTO,userID);
        return ResponseEntity.ok(msg);
    }
}

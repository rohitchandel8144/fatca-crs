package com.rcs.regulatoryComplianceSystem.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rcs.regulatoryComplianceSystem.DTO.InstitutionDTO.InstitutionRequestDTO;
import com.rcs.regulatoryComplianceSystem.DTO.InstitutionDTO.InstitutionResponseDTO;
import com.rcs.regulatoryComplianceSystem.service.InstitutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/institutions")
public class InstitutionController {

    @Autowired
    private InstitutionService institutionService;

    @PreAuthorize("hasAnyRole('ADMINISTRATOR','RFI_SUBADMIN')")
    @PostMapping("/register/{createdByUserId}")
    public ResponseEntity<InstitutionResponseDTO> registerInstitution(
            @RequestParam("institutionRequestDTO") String institutionRequestDTOJson,
            @PathVariable Long createdByUserId,
            @RequestParam("registrationLicense") MultipartFile registrationLicense,
            @RequestParam("tradeLicense") MultipartFile tradeLicense,
            @RequestParam("documents") MultipartFile documents) throws JsonProcessingException {


        ObjectMapper objectMapper = new ObjectMapper();
        InstitutionRequestDTO institutionRequestDTO = objectMapper.readValue(institutionRequestDTOJson, InstitutionRequestDTO.class);
        InstitutionResponseDTO institutionResponseDTO = (InstitutionResponseDTO) institutionService.registerInstitution(
                institutionRequestDTO, createdByUserId, registrationLicense, tradeLicense, documents);

        return ResponseEntity.ok(institutionResponseDTO);
    }


    @PreAuthorize("hasRole('SUPERADMIN') or hasRole('SUBADMIN')")
    @PostMapping("/approve/{institutionId}/{approvedByUserId}")
    public ResponseEntity<String> approveInstitution(@PathVariable Long institutionId, @PathVariable Long approvedByUserId){
        institutionService.approveInstitution(institutionId,approvedByUserId);
        return ResponseEntity.ok("institution approved");
    }

    @PreAuthorize("hasRole('SUPERADMIN') or hasRole('SUBADMIN')")
    @PostMapping("/reject/{institutionId}/{approvedByUserId}")
    public ResponseEntity<String> rejectInstitution(@PathVariable Long institutionId,@PathVariable Long approvedByUserId,@RequestBody String reason){
        institutionService.rejectInstitution(institutionId,approvedByUserId,reason);
        return ResponseEntity.ok("institution rejected");
    }

    @PreAuthorize("hasAnyRole('ADMINISTRATOR','RFI_SUBADMIN','SUPERADMIN')")
    @GetMapping("/get-institution/{institutionId}")
    public ResponseEntity<InstitutionResponseDTO> getInstitutionForReview(@PathVariable Long institutionId){
        InstitutionResponseDTO institution= institutionService.getInstitutionForReview(institutionId);
        return  ResponseEntity.ok(institution);
    }


    @PreAuthorize("hasRole('SUPERADMIN') or hasRole('SUBADMIN')")
    @GetMapping("/get-all-institutions")
    public ResponseEntity<List<InstitutionResponseDTO>> getAllPendingInstitutions() {
        List<InstitutionResponseDTO> institutions = institutionService.getAllPendingInstitutions();
        return ResponseEntity.ok(institutions);
    }

    @PreAuthorize("hasRole('SUPERADMIN') or hasRole('SUBADMIN')")
    @GetMapping("/get-all-institutions-approved")
    public ResponseEntity<List<InstitutionResponseDTO>> getAllInstitutionsApprovedByRFI() {
        List<InstitutionResponseDTO> institutions = institutionService.getAllInstitutionsApprovedByRFI();
        return ResponseEntity.ok(institutions);
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @GetMapping("/get-all-institutions-for-rfi")
    public ResponseEntity<List<InstitutionResponseDTO>> getAllInstitutionsForRFI() {
        List<InstitutionResponseDTO> institutions = institutionService.getAllInstitutionsForRFI();
        return ResponseEntity.ok(institutions);
    }


      @PreAuthorize("hasRole('SUPERADMIN') or hasRole('SUBADMIN')")
    @DeleteMapping("/delete-institution/{userId}/{institutionId}")
    public ResponseEntity<String> deleteInstitution(@PathVariable Long userId,@PathVariable Long institutionId) {
        String msg = institutionService.deleteInstitution(userId,institutionId);
        return ResponseEntity.ok(msg);
    }
}

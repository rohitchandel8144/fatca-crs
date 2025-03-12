package com.rcs.regulatoryComplianceSystem.controller;

import com.rcs.regulatoryComplianceSystem.DTO.InstitutionDTO.InstitutionRequestDTO;
import com.rcs.regulatoryComplianceSystem.DTO.InstitutionDTO.InstitutionResponseDTO;
import com.rcs.regulatoryComplianceSystem.entity.Notification;
import com.rcs.regulatoryComplianceSystem.service.InstitutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/institutions")
public class InstitutionController {

    @Autowired
    private InstitutionService institutionService;

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @PostMapping("/register/{createdByUserId}")
    public ResponseEntity<String> registerInstitution(@RequestBody InstitutionRequestDTO institutionRequestDTO , @PathVariable Long createdByUserId){
        institutionService.registerInstitution(institutionRequestDTO,createdByUserId);
        return ResponseEntity.ok("institution registered");
    }

    @PreAuthorize("hasRole('SUPERADMIN') or hasRole('SUBADMIN')")
    @PostMapping("/approve/{institutionId}/{approvedByUserId}")
    public ResponseEntity<String> approveInstitution(@PathVariable Long institutionId, @PathVariable Long approvedByUserId){
        institutionService.approveInstitution(institutionId,approvedByUserId);
        return ResponseEntity.ok("institution approved");
    }

    @PreAuthorize("hasRole('SUPERADMIN') or hasRole('SUBADMIN')")
    @PostMapping("/reject/{institutionId}/{approvedByUserId}")
    public ResponseEntity<String> rejectInstitution(@PathVariable Long institutionId,@PathVariable Long approvedByUserId){
        institutionService.rejectInstitution(institutionId,approvedByUserId);
        return ResponseEntity.ok("institution rejected");
    }

    @PreAuthorize("hasRole('SUPERADMIN')")
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

}

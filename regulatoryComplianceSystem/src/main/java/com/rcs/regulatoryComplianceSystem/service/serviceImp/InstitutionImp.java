package com.rcs.regulatoryComplianceSystem.service.serviceImp;

import com.rcs.regulatoryComplianceSystem.DTO.InstitutionDTO.InstitutionRequestDTO;
import com.rcs.regulatoryComplianceSystem.DTO.InstitutionDTO.InstitutionResponseDTO;
import com.rcs.regulatoryComplianceSystem.DTO.UserDTO.UserRequestDTO;
import com.rcs.regulatoryComplianceSystem.entity.Notification;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface InstitutionImp {
    public InstitutionResponseDTO registerInstitution(InstitutionRequestDTO institutionRequestDTO, Long createdBy, MultipartFile registrationLicense,
                                     MultipartFile tradeLicense,
                                     MultipartFile documents);
    public void approveInstitution(Long institutionId,Long approvedByUserId);

    List<InstitutionResponseDTO> getAllInstitutionsApprovedByRFI();
}

package com.rcs.regulatoryComplianceSystem.service.serviceImp;

import com.rcs.regulatoryComplianceSystem.DTO.InstitutionDTO.InstitutionRequestDTO;
import com.rcs.regulatoryComplianceSystem.DTO.InstitutionDTO.InstitutionResponseDTO;
import com.rcs.regulatoryComplianceSystem.DTO.UserDTO.UserRequestDTO;
import com.rcs.regulatoryComplianceSystem.entity.Notification;

import java.util.List;

public interface InstitutionImp {
    public  void registerInstitution(InstitutionRequestDTO institutionRequestDTO, Long createdBy);
    public void approveInstitution(Long institutionId,Long approvedByUserId);

    List<InstitutionResponseDTO> getAllInstitutionsApprovedByRFI();
}

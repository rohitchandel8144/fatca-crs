package com.rcs.regulatoryComplianceSystem.service;

import com.rcs.regulatoryComplianceSystem.DTO.InstitutionDTO.InstitutionRequestDTO;
import com.rcs.regulatoryComplianceSystem.DTO.InstitutionDTO.InstitutionResponseDTO;
import com.rcs.regulatoryComplianceSystem.DTO.UserDTO.UserRequestDTO;
import com.rcs.regulatoryComplianceSystem.entity.Institution;
import com.rcs.regulatoryComplianceSystem.entity.Notification;
import com.rcs.regulatoryComplianceSystem.entity.User;
import com.rcs.regulatoryComplianceSystem.repositories.InstitutionRepository;
import com.rcs.regulatoryComplianceSystem.repositories.NotificationRepository;
import com.rcs.regulatoryComplianceSystem.repositories.UserRepository;
import com.rcs.regulatoryComplianceSystem.service.serviceImp.InstitutionImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InstitutionService implements InstitutionImp {
    @Autowired
    private InstitutionRepository institutionRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;


    @Override
    public void registerInstitution(InstitutionRequestDTO institutionRequestDTO, Long createdByUserId) {
        User createdBy = userRepository.findById(createdByUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Institution institution = new Institution();
        institution.setName(institutionRequestDTO.getName());
        institution.setGiin(institutionRequestDTO.getGiin());
        institution.setDateOfIncorporation(institutionRequestDTO.getDateOfIncorporation());
        institution.setCountryOfIncorporation(institutionRequestDTO.getCountryOfIncorporation());
        institution.setBusinessAddressCountry(institutionRequestDTO.getBusinessAddressCountry());
        institution.setContactFirstName(institutionRequestDTO.getContactFirstName());
        institution.setContactLastName(institutionRequestDTO.getContactLastName());
        institution.setTelephone(institutionRequestDTO.getTelephone());
        institution.setBusinessAddress(institutionRequestDTO.getBusinessAddress());

        institution.setLicenseAuthority(institutionRequestDTO.getLicenseAuthority());
        institution.setTradeLicenseNumber(institutionRequestDTO.getTradeLicenseNumber());

        institution.setStatus(Institution.Status.PENDING);
        institution.setCreatedBy(createdBy);
        institution = institutionRepository.save(institution);
        createdBy.setInstitution(institution);


        Notification notification = new Notification();
        notification.setMessage("New institution registered with Id: " + institution.getInstitutionId()+" with name :"+ institution.getName());
        notification.setRecipientPanel("MINISTRY");
        notification.setStatus(Notification.Status.UNREAD);
        notificationRepository.save(notification);
    }

    @Override
    public void approveInstitution(Long institutionId, Long approvedByUserId) {
        Institution institution = institutionRepository.findById(institutionId)
                .orElseThrow(()->new RuntimeException("institution not found"));

        User user = userRepository.findById(approvedByUserId)
                        .orElseThrow(()->new UsernameNotFoundException("user not found"));

        institution.setStatus(Institution.Status.APPROVED);
        institution.setApprovedBy(user);
        institutionRepository.save(institution);

        Notification notification = new Notification();
        notification.setMessage("Your Institution '" + institution.getName() + "' has been Approved.");
        notification.setRecipientPanel("RFI");
        notification.setStatus(Notification.Status.UNREAD);
        notificationRepository.save(notification);
    }

    @Override
    public List<InstitutionResponseDTO> getAllInstitutionsApprovedByRFI() {
        List<Institution> institutions = institutionRepository.findByStatus(Institution.Status.APPROVED);
        return institutions.stream()
                .map(this::convertInstitutionToResponse)
                .collect(Collectors.toList());
    }

    public void rejectInstitution(Long institutionId, Long rejectedByUserId) {
        Institution institution = institutionRepository.findById(institutionId)
                .orElseThrow(() -> new RuntimeException("Institution not found"));
        User user = userRepository.findById(rejectedByUserId)
                        .orElseThrow(()->new UsernameNotFoundException("user not found"));

        institution.setStatus(Institution.Status.REJECTED);
        institution.setRejectedBy(user);
        institutionRepository.save(institution);

        Notification notification = new Notification();
        notification.setMessage("Your Institution '" + institution.getName() + "' has been Rejected.");
        notification.setRecipientPanel("RFI");
        notification.setStatus(Notification.Status.UNREAD);
        notificationRepository.save(notification);
    }

    public InstitutionResponseDTO getInstitutionForReview(Long institutionId) {
         Institution institution= institutionRepository.findById(institutionId)
                .orElseThrow(() -> new RuntimeException("Institution not found with ID: " + institutionId));
         return convertToDTO(institution);

    }

    private InstitutionResponseDTO convertToDTO(Institution institution) {
        InstitutionResponseDTO dto = new InstitutionResponseDTO();
        dto.setInstitutionId(institution.getInstitutionId());
        dto.setName(institution.getName());
        dto.setGiin(institution.getGiin());
        dto.setDateOfIncorporation(institution.getDateOfIncorporation());
        dto.setCountryOfIncorporation(institution.getCountryOfIncorporation());
        dto.setBusinessAddressCountry(institution.getBusinessAddressCountry());
        dto.setContactFirstName(institution.getContactFirstName());
        dto.setContactLastName(institution.getContactLastName());
        dto.setTelephone(institution.getTelephone());
        dto.setBusinessAddress(institution.getBusinessAddress());
        dto.setLicenseAuthority(institution.getLicenseAuthority());
        dto.setTradeLicenseNumber(institution.getTradeLicenseNumber());

        dto.setStatus(institution.getStatus().name());
        dto.setCreatedBy(institution.getCreatedBy().getName());
        dto.setApprovedBy(institution.getApprovedBy() != null ? institution.getApprovedBy().getName() : null);
        dto.setCreatedAt(institution.getCreatedBy().getCreatedAt());
        return dto;
    }


    public List<InstitutionResponseDTO> getAllPendingInstitutions() {
        List<Institution> institutions = institutionRepository.findByStatus(Institution.Status.PENDING);
        return institutions.stream()
                .map(this::convertInstitutionToResponse)
                .toList();
    }

    private InstitutionResponseDTO convertInstitutionToResponse(Institution institution) {
        InstitutionResponseDTO dto = new InstitutionResponseDTO();
        dto.setInstitutionId(institution.getInstitutionId());
        dto.setName(institution.getName());
        dto.setGiin(institution.getGiin());
        dto.setDateOfIncorporation(institution.getDateOfIncorporation());
        dto.setCountryOfIncorporation(institution.getCountryOfIncorporation());
        dto.setBusinessAddressCountry(institution.getBusinessAddressCountry());
        dto.setContactFirstName(institution.getContactFirstName());
        dto.setContactLastName(institution.getContactLastName());
        dto.setTelephone(institution.getTelephone());
        dto.setBusinessAddress(institution.getBusinessAddress());
        dto.setLicenseAuthority(institution.getLicenseAuthority());
        dto.setTradeLicenseNumber(institution.getTradeLicenseNumber());
        dto.setStatus(institution.getStatus().name());
        dto.setCreatedBy(institution.getCreatedBy() != null ? institution.getCreatedBy().toString() : null);
        dto.setApprovedBy(institution.getApprovedBy() != null ? institution.getApprovedBy().toString() : null);
        dto.setCreatedAt(institution.getCreatedBy() != null ? institution.getCreatedBy().getCreatedAt() : null);
        return dto;
    }

}

package com.rcs.regulatoryComplianceSystem.service;

import com.rcs.regulatoryComplianceSystem.DTO.InstitutionDTO.InstitutionRequestDTO;
import com.rcs.regulatoryComplianceSystem.DTO.InstitutionDTO.InstitutionResponseDTO;
import com.rcs.regulatoryComplianceSystem.DTO.UserDTO.UserRequestDTO;
import com.rcs.regulatoryComplianceSystem.entity.Institution;
import com.rcs.regulatoryComplianceSystem.entity.Notification;
import com.rcs.regulatoryComplianceSystem.entity.Report;
import com.rcs.regulatoryComplianceSystem.entity.User;
import com.rcs.regulatoryComplianceSystem.exceptionHandling.customExceptionHandling.UserNotFoundException;
import com.rcs.regulatoryComplianceSystem.repositories.InstitutionRepository;
import com.rcs.regulatoryComplianceSystem.repositories.NotificationRepository;
import com.rcs.regulatoryComplianceSystem.repositories.ReportRepository;
import com.rcs.regulatoryComplianceSystem.repositories.UserRepository;
import com.rcs.regulatoryComplianceSystem.service.serviceImp.InstitutionImp;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
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

    @Autowired
    private AwsService awsService;

    @Autowired
    private ReportRepository reportRepository;

    @Override
    public InstitutionResponseDTO registerInstitution(InstitutionRequestDTO institutionRequestDTO, Long createdByUserId, MultipartFile registrationLicense,
                                     MultipartFile tradeLicense,
                                     MultipartFile documents) {
        User createdBy = userRepository.findById(createdByUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

          String reg= awsService.uploadFile(registrationLicense);
          String trade= awsService.uploadFile(tradeLicense);
          String doc= awsService.uploadFile(documents);

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
        institution.setRegistrationLicense(reg);
        institution.setTradeLicense(trade);
        institution.setDocuments(doc);
        institution.setStatus(Institution.Status.PENDING);
        institution.setCreatedBy(createdBy);
        institution = institutionRepository.save(institution);
        createdBy.setInstitution(institution);

        Notification notification = new Notification();
        notification.setMessage("New institution registered with Id: " + institution.getInstitutionId()+" with name :"+ institution.getName());
        notification.setRecipientPanel("MINISTRY");
        notification.setStatus(Notification.Status.UNREAD);
        notification.setNotificationType(Notification.NotificationType.REGISTRATION);
        notificationRepository.save(notification);
        return  convertToResponseDTO(institution);

    }


    public InstitutionResponseDTO convertToResponseDTO(Institution institution) {
        if (institution == null) {
            return null;
        }

        return new InstitutionResponseDTO(
                institution.getInstitutionId(),
                institution.getName(),
                institution.getGiin(),
                institution.getCountryOfIncorporation(),
                institution.getStatus() != null ? institution.getStatus().name() : null,
                institution.getCreatedBy() != null ? institution.getCreatedBy().getName() : null,
                institution.getContactFirstName(),
                institution.getContactLastName(),
                institution.getTelephone(),
                institution.getLicenseAuthority(),
                institution.getTradeLicenseNumber(),
                institution.getDateOfIncorporation(),
                institution.getBusinessAddress()
        );
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
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public void rejectInstitution(Long institutionId, Long rejectedByUserId,String reason) {
        Institution institution = institutionRepository.findById(institutionId)
                .orElseThrow(() -> new RuntimeException("Institution not found"));
        User user = userRepository.findById(rejectedByUserId)
                        .orElseThrow(()->new UsernameNotFoundException("user not found"));

        institution.setStatus(Institution.Status.REJECTED);
        institution.setRejectedBy(user);
        institution.setRejectionReason(reason);
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
         return convertToResponseDTO(institution);

    }


    public List<InstitutionResponseDTO> getAllPendingInstitutions() {
        List<Institution> institutions = institutionRepository.findByStatus(Institution.Status.PENDING);
        return institutions.stream()
                .map(this::convertToResponseDTO)
                .toList();
    }


    @Transactional
    public String deleteInstitution(Long userId, Long institutionId) {
        // Fetch the institution to be deleted
        Institution institution = institutionRepository.findById(institutionId)
                .orElseThrow(() -> new RuntimeException("Institution not found"));

        List<User> linkedUsers = userRepository.findByInstitution(institution);
        for (User linkedUser : linkedUsers) {
            linkedUser.setInstitution(null);
            userRepository.save(linkedUser);
        }

        List<Report> reports = reportRepository.findByInstitution(institution);
        reportRepository.deleteAll(reports);

        institutionRepository.delete(institution);

        return "Institution and all references deleted successfully";
    }

    public List<InstitutionResponseDTO> getAllInstitutionsForRFI() {
        List<Institution.Status> statuses = Arrays.asList(Institution.Status.APPROVED, Institution.Status.REJECTED);
        List<Institution> institutions = institutionRepository.findByStatusIn(statuses);

        return institutions.stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

}

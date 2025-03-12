package com.rcs.regulatoryComplianceSystem.DTO.InstitutionDTO;

import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
public class InstitutionResponseDTO {

    private Long institutionId;
    private String name;
    private String giin;
    private String dateOfIncorporation;
    private String countryOfIncorporation;
    private String businessAddressCountry;
    private String contactFirstName;
    private String contactLastName;
    private String telephone;
    private String businessAddress;
    private String businessAddressStreet;
    private String licenseAuthority;
    private String tradeLicenseNumber;
    private String registrationNumber;
    private String status; // PENDING, APPROVED, REJECTED
    private String createdBy;
    private String approvedBy;
    private Date createdAt;
    private Date updatedAt;
}

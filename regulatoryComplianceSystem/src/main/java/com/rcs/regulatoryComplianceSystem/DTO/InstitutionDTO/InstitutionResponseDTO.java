package com.rcs.regulatoryComplianceSystem.DTO.InstitutionDTO;

import lombok.Getter;
import lombok.Setter;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InstitutionResponseDTO {
    private Long institutionId;
    private String name;
    private String giin;
    private String countryOfIncorporation;
    private String status;
    private String createdBy;
    private String contactFirstName;
    private String contactLastName;
    private String telephone;
    private String licenseAuthority;
    private String tradeLicenseNumber;
    private String dateOfIncorporation;
    private String businessAddress;


    public InstitutionResponseDTO(Long institutionId, String name) {
        this.institutionId=institutionId;
        this.name=name;
    }


    public InstitutionResponseDTO(Long institutionId, String name, String giin, String countryOfIncorporation,
                                  String status, String createdBy, String contactFirstName, String contactLastName,
                                  String telephone, String licenseAuthority, String tradeLicenseNumber,
                                  String dateOfIncorporation,String businessAddress) {
        this.institutionId = institutionId;
        this.name = name;
        this.giin = giin;
        this.countryOfIncorporation = countryOfIncorporation;
        this.status = status;
        this.createdBy = createdBy;
        this.contactFirstName = contactFirstName;
        this.contactLastName = contactLastName;
        this.telephone = telephone;
        this.licenseAuthority = licenseAuthority;
        this.tradeLicenseNumber = tradeLicenseNumber;
        this.dateOfIncorporation = dateOfIncorporation;
        this.businessAddress= businessAddress;
    }


}

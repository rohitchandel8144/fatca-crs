package com.rcs.regulatoryComplianceSystem.DTO.InstitutionDTO;


import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Getter
@Setter
public class InstitutionRequestDTO {

    @NotBlank(message = "Institution name is required")
    private String name;

    @NotBlank(message = "GIIN is required")
    private String giin;

    @NotBlank(message = "Date of Incorporation is required")
    private String dateOfIncorporation;

    @NotBlank(message = "Country of Incorporation is required")
    private String countryOfIncorporation;

    @NotBlank(message = "Business Address Country is required")
    private String businessAddressCountry;

    @NotBlank(message = "Contact First Name is required")
    private String contactFirstName;

    @NotBlank(message = "Contact Last Name is required")
    private String contactLastName;

    @NotBlank(message = "Telephone is required")
    @Size(min = 10, max = 15, message = "Telephone number must be between 10 and 15 characters")
    private String telephone;

    @NotBlank(message = "Business Address is required")
    private String businessAddress;

    @NotBlank(message = "License Authority is required")
    private String licenseAuthority;

    @NotBlank(message = "Trade License Number is required")
    private String tradeLicenseNumber;

    private String registrationLicense;
    private String tradeLicense ;
    private String documents;

}

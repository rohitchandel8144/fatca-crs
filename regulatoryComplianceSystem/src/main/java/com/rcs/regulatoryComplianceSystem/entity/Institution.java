package com.rcs.regulatoryComplianceSystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rcs.regulatoryComplianceSystem.entity.fatcaEntity.FatcaReport;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "institutions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Institution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private String licenseAuthority;
    private String tradeLicenseNumber;

    private String registrationLicense;
    private String tradeLicense;
    private String documents;

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "created_by", nullable = false)
    @JsonIgnore
    private User createdBy;

    @OneToMany(mappedBy = "institution", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Report> reports = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "approved_by")
    @JsonIgnore
    private User approvedBy;

    private String rejectionReason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rejected_by")
    @JsonIgnore
    private User rejectedBy;

    public enum Status { PENDING, APPROVED, REJECTED }

    @OneToMany(mappedBy = "institution", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FatcaReport> fatcaReports;
}

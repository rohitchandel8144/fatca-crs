package com.rcs.regulatoryComplianceSystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "institutions")
@Getter
@Setter
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
    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    @JsonIgnore
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "approved_by")

    @JsonIgnore
    private User approvedBy;

    @ManyToOne
    @JoinColumn(name = "rejected_by")
    @JsonIgnore
    private User rejectedBy;
    public enum Status { PENDING, APPROVED, REJECTED }
}

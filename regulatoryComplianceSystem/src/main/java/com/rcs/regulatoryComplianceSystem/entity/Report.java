package com.rcs.regulatoryComplianceSystem.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Entity
@Table(name = "reports")
@Getter
@Setter
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;

    @ManyToOne
    @JoinColumn(name = "institution_id", nullable = false)
    private Institution institution;

    @ManyToOne
    @JoinColumn(name = "uploaded_by", nullable = false)
    private User uploadedBy;

    private String regulatoryAuthority;

    private String institutionClassification;

    private String reportType;

    private String reportPeriod;

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    private String registrationDate;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private String fileData;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "approved_by", nullable = true)
    private User approvedBy;

    @ManyToOne
    @JoinColumn(name = "rejected_by")
    private User rejectedBy;

    @Temporal(TemporalType.TIMESTAMP)
    private Date approvedAt;

    private String fileName;

    private String fileType;

    private String rejectionReason;

    @Temporal(TemporalType.TIMESTAMP)
    private Date rejectedAt;

    public enum Status { PENDING, APPROVEDBYMINISTRY, REJECTEDBYMINISTRY,APPROVEDBYRFI,REJECTEDBYRFI }


}

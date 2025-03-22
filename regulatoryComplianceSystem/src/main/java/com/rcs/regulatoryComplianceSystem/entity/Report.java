package com.rcs.regulatoryComplianceSystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;

@Entity
@Table(name = "reports")
@Getter
@Setter
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "institution_id", nullable = false)
    @JsonIgnore
    private Institution institution;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploaded_by", nullable = false)
    @JsonIgnore
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
    @JsonIgnore
    private String fileData;

    @ManyToOne(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "approved_by")
    @JsonIgnore
    private User approvedBy;

    @ManyToOne(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "rejected_by")
    @JsonIgnore
    private User rejectedBy;

    @Temporal(TemporalType.TIMESTAMP)
    private Date approvedAt;

    private String fileName;
    private String fileType;
    private String rejectionReason;

    @Temporal(TemporalType.TIMESTAMP)
    private Date rejectedAt;

    public enum Status { PENDING, APPROVEDBYMINISTRY, REJECTEDBYMINISTRY, APPROVEDBYRFI, REJECTEDBYRFI }
}


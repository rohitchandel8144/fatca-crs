package com.rcs.regulatoryComplianceSystem.entity.fatcaEntity;

import com.rcs.regulatoryComplianceSystem.entity.Institution;
import com.rcs.regulatoryComplianceSystem.entity.User;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Entity
@Table(name = "fatca_reports")
@Getter
@Setter
@NoArgsConstructor
@XmlRootElement(name = "FATCA_OECD")
@XmlAccessorType(XmlAccessType.FIELD)
public class FatcaReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "message_spec_id", referencedColumnName = "id", nullable = false)  // ✅ Ensures correct FK mapping
    @XmlElement(name = "MessageSpec", required = true)
    private MessageSpec messageSpec;

    @OneToOne(cascade = CascadeType.ALL, optional = false)
    @XmlElement(name = "ReportingFI", required = true)
    private ReportingFI reportingFI;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "fatca_report_id")
    @XmlElement(name = "ReportingGroup", required = true)
    private List<ReportingGroup> reportingGroups;

    @Lob
    @Column(name = "xml_data", columnDefinition = "TEXT")
    @XmlTransient
    private String xmlData;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "institution_id", nullable = false)
    @XmlTransient// ✅ Every FATCA Report belongs to ONE Institution
    private Institution institution;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    @XmlTransient// ✅ Tracks who created the report
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    @XmlTransient// ✅ Tracks who last updated the report
    private User updatedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by")
    @XmlTransient// ✅ Tracks who approved the report
    private User approvedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rejected_by")
    @XmlTransient// ✅ Tracks who rejected the report
    private User rejectedBy;

    @Enumerated(EnumType.STRING)
    @XmlTransient
    private ReportStatus status; // ✅ Tracks report status

    public enum ReportStatus {
        PENDING, APPROVED, REJECTED
    }

    public FatcaReport(MessageSpec messageSpec, ReportingFI reportingFI, List<ReportingGroup> reportingGroups) {
        this.messageSpec = messageSpec;
        this.reportingFI = reportingFI;
        this.reportingGroups = reportingGroups;
    }
}

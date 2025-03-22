package com.rcs.regulatoryComplianceSystem.entity.fatcaEntity;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "reporting_group")
@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
public class ReportingGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "fatca_report_id", nullable = false)
    @XmlTransient

    private FatcaReport fatcaReport;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "reportingGroup", orphanRemoval = true)
    @XmlElement(name = "AccountReport", required = true)
    private List<AccountReport> accountReports;
}

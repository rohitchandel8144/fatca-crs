package com.rcs.regulatoryComplianceSystem.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "xml_transmissions")
 public class XMLTransmission {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transmissionId;

    @ManyToOne @JoinColumn(name = "report_id", nullable = false)
    private Report report;

    @ManyToOne @JoinColumn(name = "transmitted_by", nullable = false)
    private User transmittedBy;

    @Enumerated(EnumType.STRING)
    private TransmissionStatus status = TransmissionStatus.SUCCESS;

    @Temporal(TemporalType.TIMESTAMP)
    private Date transmittedAt = new Date();

    enum TransmissionStatus { SUCCESS, FAILED }
}
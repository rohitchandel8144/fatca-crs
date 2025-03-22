package com.rcs.regulatoryComplianceSystem.entity.fatcaEntity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@Entity
@Table(name = "message_spec")
@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
public class MessageSpec {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @XmlElement(name = "SendingCompanyIN", required = true)
    private String sendingCompanyIN;

    @XmlElement(name = "TransmittingCountry", required = true)
    private String transmittingCountry;

    @XmlElement(name = "ReceivingCountry", required = true)
    private String receivingCountry;

    @XmlElement(name = "MessageRefId", required = true)
    private String messageRefId;

    @XmlElement(name = "ReportingPeriod", required = true)
    private String reportingPeriod;

    @XmlElement(name = "Timestamp", required = true)
    private Date timestamp;

    public MessageSpec() {
        this.messageRefId = "MSG-" + System.currentTimeMillis();
        this.timestamp = new Date();
    }
}


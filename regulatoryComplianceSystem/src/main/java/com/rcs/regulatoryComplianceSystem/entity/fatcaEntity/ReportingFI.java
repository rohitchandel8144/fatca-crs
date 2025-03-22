package com.rcs.regulatoryComplianceSystem.entity.fatcaEntity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "reporting_fi")
@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
public class ReportingFI {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @XmlElement(name = "ResCountryCode", required = true)
    private String resCountryCode;

    @XmlElement(name = "TIN", required = true)
    private String tin;

    @XmlElement(name = "Name", required = true)
    private String name;

    @OneToOne(cascade = CascadeType.ALL, optional = false)
    @XmlElement(name = "Address", required = true)
    private Address address;
}


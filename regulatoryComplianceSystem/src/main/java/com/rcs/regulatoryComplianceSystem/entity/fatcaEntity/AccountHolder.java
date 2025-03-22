package com.rcs.regulatoryComplianceSystem.entity.fatcaEntity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;

import java.util.UUID;

@Entity
@Table(name = "account_holder")
@Getter
@Setter
@NoArgsConstructor // ✅ Required for Hibernate
@XmlAccessorType(XmlAccessType.FIELD)
public class AccountHolder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, optional = false)
    @XmlElement(name = "TIN", required = true)
    private TIN tin;

    @XmlElement(name = "Name", required = true)
    private String name;

    @OneToOne(cascade = CascadeType.ALL, optional = false)
    @XmlElement(name = "Address", required = true)
    private Address address;

    @OneToOne(cascade = CascadeType.ALL)
    @XmlElement(name = "BirthInfo", required = false)
    private BirthInfo birthInfo;

    public AccountHolder(String name, String countryCode) {
        this.name = name;
        this.tin = new TIN("TIN-" + UUID.randomUUID(), "OECD202"); // ✅ Unique TIN
        this.address = new Address("Unknown", "Unknown", countryCode, "OECD303"); // ✅ Default to Business Address
        this.birthInfo = new BirthInfo("1900-01-01", "Unknown", countryCode);
    }
}

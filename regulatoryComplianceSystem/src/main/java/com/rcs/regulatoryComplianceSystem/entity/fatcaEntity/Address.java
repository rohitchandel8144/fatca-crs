package com.rcs.regulatoryComplianceSystem.entity.fatcaEntity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import java.util.List;

@Entity
@Table(name = "address")
@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
@NoArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @XmlElement(name = "Street", required = true)
    private String street;

    @XmlElement(name = "City", required = true)
    private String city;

    @XmlElement(name = "CountryCode", required = true)
    private String countryCode;

    @XmlElement(name = "OECDLegalAddressType", required = true) // ✅ Matches XSD
    private String oecdLegalAddressType;

    public Address(String street, String city, String countryCode, String oecdLegalAddressType) {
        this.street = street;
        this.city = city;
        this.countryCode = countryCode;
        setOecdLegalAddressType(oecdLegalAddressType); // ✅ Validates OECD Address Type
    }

    public void setOecdLegalAddressType(String oecdLegalAddressType) {
        if (!List.of("OECD301", "OECD302", "OECD303", "OECD304", "OECD305").contains(oecdLegalAddressType)) {
            throw new IllegalArgumentException("Invalid Address Type: " + oecdLegalAddressType);
        }
        this.oecdLegalAddressType = oecdLegalAddressType;
    }
}

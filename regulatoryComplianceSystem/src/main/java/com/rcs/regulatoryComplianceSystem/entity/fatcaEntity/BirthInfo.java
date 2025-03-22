package com.rcs.regulatoryComplianceSystem.entity.fatcaEntity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "birth_info")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class BirthInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @XmlElement(name = "BirthDate")
    private String birthDate;

    @XmlElement(name = "City")
    private String birthCity;

    @XmlElement(name = "Country")
    private String birthCountry;

    public BirthInfo(String date, String unknown, String countryCode) {
        this.birthDate=date;
        this.birthCity= unknown;
        this.birthCountry= countryCode;
    }
}

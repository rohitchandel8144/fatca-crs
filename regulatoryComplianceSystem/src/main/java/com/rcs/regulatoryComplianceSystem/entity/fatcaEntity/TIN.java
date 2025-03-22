package com.rcs.regulatoryComplianceSystem.entity.fatcaEntity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;

import java.util.List;


@Entity
@Table(name = "tin")
@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
public class TIN {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @XmlElement(name = "TIN", required = true)
    private String tinNumber;

    @XmlElement(name = "TINType", required = true) // ✅ Matches XSD (OECD201, OECD202, etc.)
    private String tinType;

    public void setTinType(String tinType) {
        if (!List.of("OECD201", "OECD202", "OECD203", "OECD204", "OECD205", "OECD206", "OECD207", "OECD208").contains(tinType)) {
            throw new IllegalArgumentException("Invalid TIN Type: " + tinType);
        }
        this.tinType = tinType;
    }

    public TIN(String tinNumber, String tinType) {
        this.tinNumber = tinNumber;
        this.setTinType(tinType);
    }
}

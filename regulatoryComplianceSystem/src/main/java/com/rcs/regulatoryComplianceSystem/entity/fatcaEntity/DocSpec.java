package com.rcs.regulatoryComplianceSystem.entity.fatcaEntity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;

@Entity
@Table(name = "doc_spec")
@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
public class DocSpec {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @XmlElement(name = "DocTypeIndic", required = true)
    private String docTypeIndic;

    @XmlElement(name = "DocRefId", required = true)
    private String docRefId;

    public DocSpec() {}

    public DocSpec(String docTypeIndic, String docRefId) {
        this.docTypeIndic = docTypeIndic;
        this.docRefId = docRefId;
    }
}

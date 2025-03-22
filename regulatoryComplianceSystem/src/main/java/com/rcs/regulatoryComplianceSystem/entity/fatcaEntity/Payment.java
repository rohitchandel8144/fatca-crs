package com.rcs.regulatoryComplianceSystem.entity.fatcaEntity;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlTransient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "payment")
@Getter
@Setter
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @XmlElement(name = "PaymentAmount", required = true)
    private BigDecimal paymentAmount;

    @XmlElement(name = "PaymentDate", required = true)
    @Temporal(TemporalType.DATE) // ✅ Ensures correct date format
    private Date paymentDate;

    @ManyToOne
    @JoinColumn(name = "account_report_id", nullable = false)
    @XmlTransient

    private AccountReport accountReport;

    public Payment(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
        this.paymentDate = new Date(); // Defaults to today's date
    }
}

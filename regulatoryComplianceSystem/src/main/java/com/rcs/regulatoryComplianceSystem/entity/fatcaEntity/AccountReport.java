package com.rcs.regulatoryComplianceSystem.entity.fatcaEntity;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlTransient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "account_report")
@Getter
@Setter
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class AccountReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reporting_group_id", nullable = false)
    @XmlTransient
    private ReportingGroup reportingGroup;

    @OneToOne(cascade = CascadeType.ALL, optional = false)
    @XmlElement(name = "DocSpec", required = true)
    private DocSpec docSpec;  // ✅ Required field

    @XmlElement(name = "AccountNumber", required = true)
    private String accountNumber;

    @XmlElement(name = "AccountBalance", required = true)
    private BigDecimal accountBalance;

    @OneToOne(cascade = CascadeType.ALL, optional = false)
    @XmlElement(name = "AccountHolder", required = true)
    private AccountHolder accountHolder;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "accountReport")
    @XmlElement(name = "Payment", required = false)
    private List<Payment> payments;

    public AccountReport(String accountNumber, BigDecimal accountBalance, AccountHolder accountHolder, List<Payment> payments) {
        this.docSpec = new DocSpec("FATCA", "DOC-" + UUID.randomUUID().toString()); // Automatically generates DocSpec
        this.accountNumber = accountNumber;
        this.accountBalance = accountBalance;
        this.accountHolder = accountHolder;
        this.payments = payments;

        // ✅ Set Payment Relationships
        for (Payment payment : this.payments) {
            payment.setAccountReport(this);
        }
    }
}

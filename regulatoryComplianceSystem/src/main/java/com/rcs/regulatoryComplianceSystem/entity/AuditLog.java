package com.rcs.regulatoryComplianceSystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "audit_logs")
@Getter
@Setter
public class AuditLog {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logId;

    @ManyToOne @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String action;

    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp = new Date();

    public AuditLog(User user, String action) {
        this.user = user;
        this.action = action;
        this.timestamp = new Date();
    }

    public AuditLog() {
    }
}

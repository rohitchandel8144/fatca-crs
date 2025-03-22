package com.rcs.regulatoryComplianceSystem.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Entity
@Table(name = "notifications")
@Getter
@Setter
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;

    @Enumerated(EnumType.STRING)
    private Status status = Status.UNREAD;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date();

    private String recipientPanel;

    private String reason;


    public enum Status {
        UNREAD, READ
    }

    public enum NotificationType {
        REGISTRATION,
        REPORT
    }
}

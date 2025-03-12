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

    private String message;  // Notification content

    @Enumerated(EnumType.STRING)
    private Status status = Status.UNREAD;  // UNREAD / READ

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;  // REGISTRATION or REPORT

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date();  // Timestamp when notification is created

    private String recipientPanel; // "MINISTRY" or "RFI"

    public enum Status {
        UNREAD, READ
    }

    public enum NotificationType {
        REGISTRATION, // Notifications related to user or institution registration
        REPORT        // Notifications related to report submissions or approvals
    }
}

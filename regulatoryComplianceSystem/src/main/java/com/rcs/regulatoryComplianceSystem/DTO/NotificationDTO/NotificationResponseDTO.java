package com.rcs.regulatoryComplianceSystem.DTO.NotificationDTO;


import com.rcs.regulatoryComplianceSystem.entity.Notification;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter

public class NotificationResponseDTO {

    private Long id;
    private String message;
    private Notification.Status status;
    private Notification.NotificationType notificationType;
    private Date createdAt;
    private String recipientPanel;
    private String reason;

    public NotificationResponseDTO(String message,
                                   Notification.Status status,
                                   Notification.NotificationType notificationType,
                                   Date createdAt,
                                   String recipientPanel,
                                   String reason) {
        this.message=message;
        this.status=status;
        this.notificationType=notificationType;
        this.createdAt=createdAt;
        this.recipientPanel=recipientPanel;
        this.reason=reason;
    }
}
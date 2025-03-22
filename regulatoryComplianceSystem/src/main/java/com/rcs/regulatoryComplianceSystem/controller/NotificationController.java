package com.rcs.regulatoryComplianceSystem.controller;

import com.rcs.regulatoryComplianceSystem.DTO.NotificationDTO.NotificationResponseDTO;
import com.rcs.regulatoryComplianceSystem.entity.Notification;
import com.rcs.regulatoryComplianceSystem.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PreAuthorize("hasRole('SUPERADMIN')")
    @GetMapping("/ministry-panel/{recipientPanel}")
    public ResponseEntity<List<NotificationResponseDTO>> getMinistryNotifications(@PathVariable String recipientPanel){
        List<NotificationResponseDTO> notifications = notificationService.getUnreadNotifications(recipientPanel);
        return ResponseEntity.ok(notifications);
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @GetMapping("/rfi-panel/{recipientPanel}")
    public ResponseEntity<List<NotificationResponseDTO>> getRFINotifications(@PathVariable String recipientPanel){
        List<NotificationResponseDTO> notifications = notificationService.getUnreadNotifications(recipientPanel);
        return ResponseEntity.ok(notifications);
    }

    @PutMapping("/mark-read/{id}")
    public ResponseEntity<String> markNotificationAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return  ResponseEntity.ok("notification updated");
    }
}


package com.rcs.regulatoryComplianceSystem.service.serviceImp;

import com.rcs.regulatoryComplianceSystem.entity.Notification;

import java.util.List;


public interface NotificationServiceImp {
    public void  createNotification(String message, String recipientName, Notification.NotificationType type);
    public List<Notification> getUnreadNotifications(String recipientPanel);
    public void markAsRead(Long notificationId);

}

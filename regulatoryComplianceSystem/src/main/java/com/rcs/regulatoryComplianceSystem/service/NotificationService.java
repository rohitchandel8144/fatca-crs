package com.rcs.regulatoryComplianceSystem.service;

import com.rcs.regulatoryComplianceSystem.entity.Notification;
import com.rcs.regulatoryComplianceSystem.repositories.NotificationRepository;
import com.rcs.regulatoryComplianceSystem.service.serviceImp.NotificationServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class NotificationService implements NotificationServiceImp {
    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public void  createNotification(String message, String recipientName,Notification.NotificationType type){
        Notification notification = new Notification();
        notification.setMessage(message);
        notification.setRecipientPanel(recipientName);
        notification.setNotificationType(type);
        notificationRepository.save(notification);
    }

    @Override
    public List<Notification> getUnreadNotifications(String recipientPanel){
        return notificationRepository
                .findByRecipientPanelAndStatus(recipientPanel, Notification.Status.UNREAD);
    }

    @Override
    public void markAsRead(Long notificationId){
       Notification notification =notificationRepository.findById(notificationId).orElseThrow(null);
        if(notification!=null){
            notification.setStatus(Notification.Status.READ);
            notificationRepository.save(notification);
        }
    }
}

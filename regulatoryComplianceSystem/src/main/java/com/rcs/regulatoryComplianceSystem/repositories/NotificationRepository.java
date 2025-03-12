package com.rcs.regulatoryComplianceSystem.repositories;

import com.rcs.regulatoryComplianceSystem.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {
    List<Notification> findByRecipientPanelAndStatus(String RecipientPanel, Notification.Status status);
}

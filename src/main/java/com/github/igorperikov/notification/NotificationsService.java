package com.github.igorperikov.notification;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationsService {
    private final NotificationRepository notificationRepository;

    public NotificationsService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public List<Notification> getAll(String userId) {
        return notificationRepository.getAll(userId);
    }

    public void create(Notification notification) {
        notificationRepository.create(notification);
    }

    public void delete(String userId, String notificationId) {
        notificationRepository.delete(userId, notificationId);
    }
}

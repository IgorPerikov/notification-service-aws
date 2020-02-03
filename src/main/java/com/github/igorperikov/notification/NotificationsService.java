package com.github.igorperikov.notification;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationsService {
    private final NotificationRepository notificationRepository;

    public NotificationsService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public List<Notification> getAll() {
        return notificationRepository.getAll();
    }

    public void create(Notification notification) {
        notificationRepository.create(notification);
    }

    public void update(String notificationId, Notification notification) {
        notificationRepository.update(notificationId, notification);
    }

    public void delete(String notificationId) {
        notificationRepository.delete(notificationId);
    }
}

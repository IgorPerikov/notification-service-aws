package com.github.igorperikov.notification.service;

import com.github.igorperikov.notification.domain.Notification;
import com.github.igorperikov.notification.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class NotificationsService {
    private final NotificationRepository notificationRepository;

    public NotificationsService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public List<Notification> findAll(String userId) {
        return notificationRepository.findForUser(userId);
    }

    public void create(Notification notification) {
        notificationRepository.create(notification);
    }

    public void delete(String userId, String notificationId) {
        Notification byId = notificationRepository.findById(notificationId);
        if (!byId.userId.equals(userId)) {
            throw new IllegalArgumentException(
                    String.format("notification %s doesn't belong to user %s", notificationId, userId)
            );
        }
        notificationRepository.delete(notificationId);
    }

    public List<Notification> findSubjectsToNotifyAndDelete() {
        return findSubjectsToNotifyAndDelete(5);
    }

    public List<Notification> findSubjectsToNotifyAndDelete(int limit) {
        List<Notification> earlierThan = notificationRepository.findEarlierThan(Instant.now().getEpochSecond(), limit);
        for (Notification notification : earlierThan) {
            notificationRepository.delete(notification.notificationId);
        }
        return earlierThan;
    }
}

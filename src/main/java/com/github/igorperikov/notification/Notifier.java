package com.github.igorperikov.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Notifier {
    private static final Logger log = LoggerFactory.getLogger(Notifier.class);

    private final NotificationsService notificationsService;

    public Notifier(NotificationsService notificationsService) {
        this.notificationsService = notificationsService;
    }

    @Scheduled(fixedDelay = 1 * 60 * 1000, initialDelay = 30 * 1000)
    public void pollNotifications() {
        List<Notification> notifications = notificationsService.findSubjectsToNotifyAndDelete();
        for (Notification notification : notifications) {
            notify(notification);
        }
    }

    private void notify(Notification notification) {
        // TODO:
        log.info("{}", notification);
    }
}

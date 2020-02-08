package com.github.igorperikov.notification.service;

import com.github.igorperikov.notification.domain.Notification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Notifier {
    private final NotificationsService notificationsService;
    private final SmsSenderService smsSenderService;

    public Notifier(NotificationsService notificationsService, SmsSenderService smsSenderService) {
        this.notificationsService = notificationsService;
        this.smsSenderService = smsSenderService;
    }

    @Scheduled(fixedDelay = 1 * 60 * 1000, initialDelay = 30 * 1000)
    public void pollNotifications() {
        List<Notification> notifications = notificationsService.findSubjectsToNotifyAndDelete();
        for (Notification notification : notifications) {
            smsSenderService.sendSms(notification.phoneNumber, notification.text);
        }
    }
}

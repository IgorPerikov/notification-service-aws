package com.github.igorperikov.notification;

import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@Repository
// TODO:
public class NotificationRepository {
    public List<Notification> getAll() {
        return Collections.emptyList();
    }

    public void create(Notification notification) {

    }

    public void update(String notificationId, Notification notification) {

    }

    public void delete(String notificationId) {

    }
}

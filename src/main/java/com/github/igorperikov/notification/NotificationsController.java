package com.github.igorperikov.notification;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationsController {
    private final NotificationsService notificationsService;

    public NotificationsController(NotificationsService notificationsService) {
        this.notificationsService = notificationsService;
    }

    @GetMapping
    public List<Notification> getAll() {
        return notificationsService.getAll();
    }

    @PostMapping
    public void create(@RequestBody Notification notification) {
        notificationsService.create(notification);
    }

    @PatchMapping("/{notificationId}")
    public void update(
            @PathVariable String notificationId,
            @RequestBody Notification notification
    ) {
        notificationsService.update(notificationId, notification);
    }

    @DeleteMapping("/{notificationId}")
    public void delete(@PathVariable String notificationId) {
        notificationsService.delete(notificationId);
    }
}

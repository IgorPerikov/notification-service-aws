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
    public List<Notification> getAll(@RequestParam String userId) {
        return notificationsService.getAll(userId);
    }

    @PostMapping
    public void create(@RequestBody Notification notification) {
        notificationsService.create(notification);
    }

    @DeleteMapping("/{notificationId}")
    public void delete(@PathVariable String notificationId, @RequestParam String userId) {
        notificationsService.delete(userId, notificationId);
    }
}

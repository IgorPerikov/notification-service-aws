package com.github.igorperikov.notification.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class Notification {
    public final String notificationId;
    public final String userId;
    public final String text;
    public final long timestamp;
    public final String phoneNumber;

    @JsonCreator
    public Notification(
            @JsonProperty(value = "userId", required = true) String userId,
            @JsonProperty(value = "text", required = true) String text,
            @JsonProperty(value = "timestamp", required = true) long timestamp,
            @JsonProperty(value = "phoneNumber", required = true) String phoneNumber
    ) {
        this(UUID.randomUUID().toString(), userId, text, timestamp, phoneNumber);
    }

    public Notification(String notificationId, String userId, String text, long timestamp, String phoneNumber) {
        this.notificationId = notificationId;
        this.userId = userId;
        this.text = text;
        this.timestamp = timestamp;
        this.phoneNumber = phoneNumber;
    }
}

package com.github.igorperikov.notification;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class Notification {
    public final String notificationId;
    public final String userId;
    public final String text;
    public final long timestamp;

    @JsonCreator
    public Notification(
            @JsonProperty("userId") String userId,
            @JsonProperty("text") String text,
            @JsonProperty("timestamp") long timestamp
    ) {
        this(UUID.randomUUID().toString(), userId, text, timestamp);
    }

    public Notification(String notificationId, String userId, String text, long timestamp) {
        this.notificationId = notificationId;
        this.userId = userId;
        this.text = text;
        this.timestamp = timestamp;
    }
}

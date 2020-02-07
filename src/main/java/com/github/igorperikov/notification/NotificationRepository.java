package com.github.igorperikov.notification;

import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class NotificationRepository {
    private static final String TABLE_NAME = "notifications";

    private static final String NOTIFICATION_ID_COLUMN = "notification_id";
    private static final String USER_ID_COLUMN = "user_id";
    private static final String TIMESTAMP_COLUMN = "timestamp";
    private static final String TEXT_COLUMN = "text";

    private final DynamoDbClient dynamoDbClient;

    public NotificationRepository(DynamoDbClient dynamoDbClient) {
        this.dynamoDbClient = dynamoDbClient;
    }

    public List<Notification> getAll(String userId) {
        ScanRequest scanRequest = ScanRequest.builder()
                .tableName(TABLE_NAME)
                .select(Select.ALL_ATTRIBUTES)
                .expressionAttributeValues(Map.of(":userId", AttributeValue.builder().s(userId).build()))
                .filterExpression("user_id=:userId")
                .build();

        return dynamoDbClient.scan(scanRequest).items().stream().map(this::convert).collect(Collectors.toList());
    }

    public void create(Notification notification) {
        PutItemRequest item = PutItemRequest.builder()
                .tableName(TABLE_NAME)
                .item(Map.of(
                        USER_ID_COLUMN, AttributeValue.builder().s(notification.userId).build(),
                        NOTIFICATION_ID_COLUMN, AttributeValue.builder().s(notification.notificationId).build(),
                        TEXT_COLUMN, AttributeValue.builder().s(notification.text).build(),
                        TIMESTAMP_COLUMN, AttributeValue.builder().n(String.valueOf(notification.timestamp)).build()
                ))
                .build();
        dynamoDbClient.putItem(item);
    }

    public void delete(String userId, String notificationId) {
        GetItemRequest getItemRequest = GetItemRequest.builder()
                .tableName(TABLE_NAME)
                .key(Map.of(NOTIFICATION_ID_COLUMN, AttributeValue.builder().s(notificationId).build()))
                .build();
        Notification notification = convert(dynamoDbClient.getItem(getItemRequest).item());
        if (!notification.userId.equals(userId)) {
            throw new IllegalArgumentException("You cannot delete notification, which doesn't belong to you");
        }

        DeleteItemRequest deleteItemRequest = DeleteItemRequest.builder()
                .tableName(TABLE_NAME)
                .key(Map.of(NOTIFICATION_ID_COLUMN, AttributeValue.builder().s(notificationId).build()))
                .build();
        dynamoDbClient.deleteItem(deleteItemRequest);
    }

    private Notification convert(Map<String, AttributeValue> attributes) {
        return new Notification(
                attributes.get(NOTIFICATION_ID_COLUMN).s(),
                attributes.get(USER_ID_COLUMN).s(),
                attributes.get(TEXT_COLUMN).s(),
                Long.parseLong(attributes.get(TIMESTAMP_COLUMN).n())
        );
    }
}

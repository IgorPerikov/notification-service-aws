package com.github.igorperikov.notification.repository;

import com.github.igorperikov.notification.domain.Notification;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class NotificationRepository {
    private static final String TABLE_NAME = "notifications";

    private static final String NOTIFICATION_ID_COLUMN = "notification_id";
    private static final String USER_ID_COLUMN = "user_id";
    private static final String TIMESTAMP_COLUMN = "notification_timestamp";
    private static final String TEXT_COLUMN = "text";
    private static final String PHONE_NUMBER_COLUMN = "phone_number";

    private final DynamoDbClient dynamoDbClient;

    public NotificationRepository(DynamoDbClient dynamoDbClient) {
        this.dynamoDbClient = dynamoDbClient;
    }

    public List<Notification> findForUser(String userId) {
        ScanRequest scanRequest = ScanRequest.builder()
                .tableName(TABLE_NAME)
                .select(Select.ALL_ATTRIBUTES)
                .expressionAttributeValues(Map.of(
                        ":userId", AttributeValue.builder().s(userId).build()
                ))
                .filterExpression("user_id=:userId")
                .build();

        return dynamoDbClient.scan(scanRequest).items().stream().map(this::convert).collect(Collectors.toList());
    }

    public List<Notification> findEarlierThan(long epochSecond, int limit) {
        ScanRequest scanRequest = ScanRequest.builder()
                .tableName(TABLE_NAME)
                .limit(limit)
                .select(Select.ALL_ATTRIBUTES)
                .expressionAttributeValues(Map.of(
                        ":ts", AttributeValue.builder().n(String.valueOf(epochSecond)).build()
                ))
                .filterExpression("notification_timestamp<=:ts")
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
                        PHONE_NUMBER_COLUMN, AttributeValue.builder().s(notification.phoneNumber).build(),
                        TIMESTAMP_COLUMN, AttributeValue.builder().n(String.valueOf(notification.timestamp)).build()
                ))
                .build();
        dynamoDbClient.putItem(item);
    }

    public Notification findById(String notificationId) {
        GetItemRequest getItemRequest = GetItemRequest.builder()
                .tableName(TABLE_NAME)
                .key(Map.of(NOTIFICATION_ID_COLUMN, AttributeValue.builder().s(notificationId).build()))
                .build();
        return convert(dynamoDbClient.getItem(getItemRequest).item());
    }

    public void delete(String notificationId) {
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
                Long.parseLong(attributes.get(TIMESTAMP_COLUMN).n()),
                attributes.get(PHONE_NUMBER_COLUMN).s()
        );
    }
}

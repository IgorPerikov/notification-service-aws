package com.github.igorperikov.notification.service;

import com.github.igorperikov.notification.domain.Notification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.cloudwatch.CloudWatchClient;
import software.amazon.awssdk.services.cloudwatch.model.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class Notifier {
    private final NotificationsService notificationsService;
    private final SmsSenderService smsSenderService;
    private final CloudWatchClient cloudWatchClient;

    public Notifier(
            NotificationsService notificationsService,
            SmsSenderService smsSenderService,
            CloudWatchClient cloudWatchClient
    ) {
        this.notificationsService = notificationsService;
        this.smsSenderService = smsSenderService;
        this.cloudWatchClient = cloudWatchClient;
    }

    @Scheduled(fixedDelay = 20 * 1000, initialDelay = 20 * 1000)
    public void pollNotifications() {
        List<Notification> notifications = notificationsService.findSubjectsToNotifyAndDelete();
        sendMetric(notifications.stream().map(n -> n.userId).collect(Collectors.toList()));
        for (Notification notification : notifications) {
            smsSenderService.sendSms(notification.phoneNumber, notification.text);
        }
    }

    private void sendMetric(List<String> userIds) {
        if (userIds.isEmpty()) {
            return;
        }

        Map<String, Long> userByCount = userIds.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        List<MetricDatum> metrics = new ArrayList<>();
        for (Map.Entry<String, Long> countByUserId : userByCount.entrySet()) {
            Dimension dimension = Dimension.builder().name("user_id").value(countByUserId.getKey()).build();
            metrics.add(MetricDatum.builder()
                    .metricName("by_user")
                    .unit(StandardUnit.COUNT)
                    .timestamp(Instant.now())
                    .value(Double.valueOf(countByUserId.getValue()))
                    .dimensions(dimension)
                    .build());
        }

        PutMetricDataRequest request = PutMetricDataRequest.builder()
                .namespace("new_notifications")
                .metricData(metrics)
                .build();

        cloudWatchClient.putMetricData(request);
    }
}

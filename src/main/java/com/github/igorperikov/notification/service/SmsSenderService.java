package com.github.igorperikov.notification.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.cloudwatch.CloudWatchClient;
import software.amazon.awssdk.services.cloudwatch.model.Dimension;

import java.util.Random;

@Service
public class SmsSenderService {
    private static final Logger log = LoggerFactory.getLogger(SmsSenderService.class);
    private static final Random RANDOM = new Random();

    public void sendSms(String phoneNumber, String messageText) {
        try {
            Thread.sleep(50 + RANDOM.nextInt(200));
        } catch (InterruptedException ignored) {}
        log.info("SMS was sent to {} with text {}", phoneNumber, messageText);
    }
}

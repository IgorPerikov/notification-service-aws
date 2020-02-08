package com.github.igorperikov.notification.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SmsSenderService {
    private static final Logger log = LoggerFactory.getLogger(SmsSenderService.class);

    public void sendSms(String phoneNumber, String messageText) {
        log.info("SMS was sent to {} with text {}", phoneNumber, messageText);
    }
}

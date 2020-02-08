package com.github.igorperikov.notification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
public class NotificationServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }

    @Configuration
    @EnableSwagger2
    @EnableScheduling
    public static class AppConfiguration {
        public static final Region REGION = Region.US_EAST_1;

        @Bean
        public DynamoDbClient dynamoDbClient() {
            return DynamoDbClient.builder().region(REGION).build();
        }
    }
}

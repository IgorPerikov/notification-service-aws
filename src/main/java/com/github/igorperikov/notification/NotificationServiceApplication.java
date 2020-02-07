package com.github.igorperikov.notification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
    public static class AppConfiguration {
        @Bean
        public DynamoDbClient dynamoDbClient() {
            return DynamoDbClient.builder().region(Region.US_EAST_1).build();
        }
    }
}

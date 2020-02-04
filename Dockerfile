FROM openjdk:11.0.6-jdk-stretch AS BUILD_IMAGE
WORKDIR /
COPY . /
RUN ./mvnw package

FROM openjdk:11.0.6-jre-stretch
COPY --from=BUILD_IMAGE /target/notification-service.jar .
CMD ["java", "-Xmx750m", "-Xms500m", "-XX:+UseStringDeduplication", "-jar", "notification-service.jar"]

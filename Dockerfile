# Builder stage
FROM openjdk:17-jdk-slim as builder
WORKDIR application
ARG WAR_FILE=target/*.war
COPY ${WAR_FILE} application.war
RUN java -Djarmode=layertools -jar application.war extract

# Final stage
FROM openjdk:17-jdk-alpine
WORKDIR application
COPY --from=builder application/dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.WarLauncher"]
EXPOSE 8080
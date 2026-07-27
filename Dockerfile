# 1. Build Stage
FROM --platform=linux/amd64 gradle:8.5-jdk17 AS builder
WORKDIR /app
COPY . .
RUN gradle build --no-daemon -x test

# 2. Run Stage (AWS EC2 및 Apple Silicon 양쪽 모두 지원하는 Amazon Corretto)
FROM --platform=linux/amd64 amazoncorretto:17-alpine
WORKDIR /app
COPY --from=builder /app/build/libs/*-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
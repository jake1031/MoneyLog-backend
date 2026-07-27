FROM amazoncorretto:17-alpine
WORKDIR /app

# 정확한 jar 파일 하나만 지정해서 복사
COPY build/libs/MoneyLog-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

# Xmx를 300m~384m 정도로 다이어트 + Cgroup 메모리 감지 옵션
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-Xms128m", "-Xmx300m", "-jar", "app.jar"]
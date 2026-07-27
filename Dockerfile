FROM amazoncorretto:17-alpine
WORKDIR /app

# 맥북에서 빌드된 jar 파일을 복사
COPY build/libs/*-SNAPSHOT.jar app.jar

EXPOSE 8080

# 메모리 폭주 방지(-Xmx512m) 옵션 포함해서 실행
ENTRYPOINT ["java", "-Xms256m", "-Xmx512m", "-jar", "app.jar"]
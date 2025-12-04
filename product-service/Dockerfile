FROM amazoncorretto:17-alpine
# healthcheck에 필요한 curl 설치
RUN apk add --no-cache curl
ENV SPRING_PROFILES_ACTIVE=docker
WORKDIR /app
COPY build/libs/product-service-0.0.1-SNAPSHOT.jar app.jar
#ENTRYPOINT ["java", "-jar", "app.jar"]
#메모리 제한
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]


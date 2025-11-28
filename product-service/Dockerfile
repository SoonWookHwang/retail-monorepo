FROM amazoncorretto:17-alpine
ENV SPRING_PROFILES_ACTIVE=docker
WORKDIR /app
COPY build/libs/product-service-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]

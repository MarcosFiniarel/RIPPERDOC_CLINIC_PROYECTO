FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY target/cyber-finance-service-0.0.1-SNAPSHOT.jar app.jar

# Expone el puerto asignado a este microservicio
EXPOSE 8084

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
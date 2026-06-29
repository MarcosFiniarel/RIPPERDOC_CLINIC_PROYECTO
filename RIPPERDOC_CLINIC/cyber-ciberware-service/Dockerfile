FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Copia el JAR del microservicio de Ciberware
COPY target/cyber-ciberware-service-0.0.1-SNAPSHOT.jar app.jar

# Expone el puerto asignado a este microservicio
EXPOSE 8082

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
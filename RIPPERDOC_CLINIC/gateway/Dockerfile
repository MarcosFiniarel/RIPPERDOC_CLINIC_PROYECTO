FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Copia el JAR del Gateway
COPY target/gateway-0.0.1-SNAPSHOT.jar app.jar

# Expone el puerto del API Gateway
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
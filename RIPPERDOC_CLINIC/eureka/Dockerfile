FROM eclipse-temurin:21-jdk-alpine

# Directorio de trabajo interno
WORKDIR /app

# Copia el JAR de Eureka desde su carpeta target
COPY target/eureka-0.0.1-SNAPSHOT.jar app.jar

# Expone el puerto oficial de Eureka
EXPOSE 8761

# Ejecuta el servidor
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
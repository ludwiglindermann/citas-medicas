FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY target/citasmedicas-0.0.1-SNAPSHOT.jar app.jar
COPY wallet /app/wallet
ENTRYPOINT ["java", "-jar", "app.jar"]
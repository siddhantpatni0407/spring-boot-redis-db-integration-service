# Use official OpenJDK 21 base image
FROM eclipse-temurin:21-jdk-alpine

# Set working directory in container
WORKDIR /app

# Copy Gradle wrapper and build files
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# Copy source code
COPY src src

# Make gradlew executable
RUN chmod +x gradlew

# Build the application inside container
RUN ./gradlew clean bootJar -x test

# Expose port 8080
EXPOSE 8080

# Default command to run Spring Boot jar
ENTRYPOINT ["java", "-jar", "build/libs/spring-boot-redis-db-integration-service-0.0.1-SNAPSHOT.jar"]

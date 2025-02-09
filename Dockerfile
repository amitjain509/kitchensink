# Use official Java 23 runtime as base image
FROM eclipse-temurin:23-jre

# Set working directory
WORKDIR /app

# Copy the JAR file from the target directory
COPY build/libs/kitchensink-*.jar app.jar

# Expose port 8080
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

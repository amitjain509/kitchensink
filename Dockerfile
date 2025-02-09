# Stage 1: Build the JAR inside Docker
FROM gradle:jdk23 AS builder

WORKDIR /app

# Copy only Gradle-related files first (Leverage caching)
COPY gradlew settings.gradle build.gradle ./
COPY gradle gradle

# Give execution permission to Gradle wrapper
RUN chmod +x gradlew

# Copy the source code
COPY src src

# Build the bootJar
RUN ./gradlew bootJar

# Stage 2: Use official Java 23 runtime as base image
FROM eclipse-temurin:23-jre

# Set working directory
WORKDIR /app

# Copy the built JAR from the builder stage
COPY --from=builder /app/build/libs/kitchensink-*.jar app.jar

# Copy SSL certificates
COPY config/certs/ca.crt /etc/ssl/
COPY config/certs/ /etc/

# Import CA certificate into Java TrustStore (Password from ENV)
RUN export CERT_PASS=${CERT_PASS:-changeit} && \
    keytool -import -trustcacerts \
    -keystore /opt/java/openjdk/lib/security/cacerts \
    -storepass "$CERT_PASS" -noprompt \
    -alias mongoDBRootCA -file /etc/ssl/ca.crt

# Expose port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

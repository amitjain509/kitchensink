# Use official Java 23 runtime as base image
FROM eclipse-temurin:23-jre

# Set working directory
WORKDIR /app

COPY config/certs/ca.crt /etc/ssl/

RUN keytool -import -trustcacerts -keystore /opt/java/openjdk/lib/security/cacerts -storepass "${SSL_KEYSTORE_PASSWORD}" -noprompt -alias mongoDBRootCA -file /etc/ssl/ca.crt

COPY build/libs/kitchensink-*.jar app.jar

COPY config/certs/ /etc/

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

# Use Java 21 runtime
FROM eclipse-temurin:21-jdk

# Create a working directory inside the container
WORKDIR /app

# Copy the built JAR into the container
COPY target/incident-management-0.0.1-SNAPSHOT.jar app.jar

# Expose Spring Boot port
EXPOSE 8080

# Start the application
ENTRYPOINT ["java","-jar","app.jar"]
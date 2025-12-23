# Use an official Java 17 base image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy the JAR file (built later by Maven)
COPY target/demo-0.0.1-SNAPSHOT.jar app.jar

# Expose port 8080 for the app
EXPOSE 8080

# Run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]

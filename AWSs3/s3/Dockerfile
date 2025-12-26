# Use an official Eclipse Temurin runtime as a parent image
FROM eclipse-temurin:17-jdk-jammy AS build
# Set the working directory inside the container
WORKDIR /app
# Copy the Maven wrapper and project files to the container
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
# Copy the source code to the container
COPY src src
# Build the application using Maven
RUN ./mvnw -B -DskipTests package

# Use a smaller runtime image for the final container
# Use a smaller base image for the final stage
FROM eclipse-temurin:17-jre-jammy
# Set the working directory inside the container
WORKDIR /app
# Copy the built jar file from the build stage
COPY --from=build /app/target/spring-s3-demo-0.0.1-SNAPSHOT.jar app.jar
# Expose the application port
EXPOSE 8080
# Define the command to run the application
ENTRYPOINT ["java","-jar","/app/app.jar"]

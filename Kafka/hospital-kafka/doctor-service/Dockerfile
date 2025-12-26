
# doctor-service/Dockerfile
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY target/doctor-service-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]

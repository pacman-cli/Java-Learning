# Backend Dockerfile (Spring Boot 3 + Java 17)
FROM maven:3.9.9-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn -q -DskipTests dependency:go-offline
COPY src ./src
RUN mvn -q -DskipTests package

FROM eclipse-temurin:17-jre AS runner
WORKDIR /app
ENV JAVA_OPTS="-Xms256m -Xmx512m"
COPY --from=builder /app/target/portfolio-backend-0.1.0.jar ./app.jar
EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]


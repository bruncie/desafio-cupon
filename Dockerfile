# Multi-stage Dockerfile for a Spring Boot Maven project
# Build stage
FROM maven:3.9.4-eclipse-temurin-17 AS builder
WORKDIR /workspace
COPY pom.xml mvnw ./
COPY .mvn .mvn
# Copy source
COPY src ./src
# Ensure Maven wrapper is executable
RUN if [ -f mvnw ]; then chmod +x mvnw; fi
# Build application (skip tests for faster image build)
RUN mvn -B -DskipTests package

# Run stage
FROM eclipse-temurin:17-jre
WORKDIR /app
# Copy jar built by maven
COPY --from=builder /workspace/target/desafio-0.0.1-SNAPSHOT.jar app.jar
# Expose the default Spring Boot port
EXPOSE 8080
# Create directory for H2 file DB
RUN mkdir -p /data
VOLUME ["/data"]
ENTRYPOINT ["java","-jar","/app/app.jar"]


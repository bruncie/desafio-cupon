# Build stage
FROM maven:3.9.4-eclipse-temurin-17 AS builder
WORKDIR /workspace

COPY pom.xml .
COPY src ./src

# Build do jar
RUN mvn -B -DskipTests package

# Run stage
FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=builder /workspace/target/desafio-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

RUN mkdir -p /data
VOLUME ["/data"]

ENTRYPOINT ["java","-jar","/app/app.jar"]

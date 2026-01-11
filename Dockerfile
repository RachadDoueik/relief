# --- Stage 1: Build stage ---
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# Copy maven wrapper and pom first to cache dependencies
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
RUN chmod +x mvnw && ./mvnw dependency:go-offline -B

# Copy source and build JAR
COPY src src
RUN ./mvnw clean package -DskipTests

# --- Stage 2: Production runtime stage ---
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Create a non-root user and the upload directory
RUN addgroup -S spring && adduser -S spring -G spring && \
    mkdir -p /app/uploads && chown spring:spring /app/uploads

USER spring:spring

# Copy the built JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

# ==========================================
# Stage 1: Build Stage
# ==========================================
FROM maven:3.9.6-eclipse-temurin-17-alpine AS builder

WORKDIR /app

# 1. Copy pom.xml and resolve dependencies (leveraging Docker layer caching)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# 2. Copy source code and package the application (skipping tests to speed up the build)
COPY src ./src
RUN mvn clean package -DskipTests

# ==========================================
# Stage 2: Runtime Stage
# ==========================================
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# 3. Copy only the built JAR file from the builder stage
# This dynamically captures the compiled artifact (e.g., AtelierStore-0.0.1-SNAPSHOT.jar)
COPY --from=builder /app/target/*.jar app.jar

# 4. Run the application as a non-root user for security compliance
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

EXPOSE 8080

# 5. Execution command (optimized with G1 Garbage Collector for container environments)
ENTRYPOINT ["java", "-XX:+UseG1GC", "-jar", "app.jar"]
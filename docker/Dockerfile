# ==============================================================================
# Multi-Stage Dockerfile for Vaccination Reminder Portal (VaxGuard)
# Project Author: Gayatri Patil (23102A0016, Roll 15)
# ==============================================================================

# STAGE 1: Build & Package with Maven and Eclipse Temurin JDK 21
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /build

# Copy Maven Wrapper and POM first to leverage Docker layer caching
COPY pom.xml .
COPY mvnw .
COPY mvnw.cmd .
COPY .mvn .mvn

# Download dependencies offline
RUN chmod +x ./mvnw || true

# Copy full application source code
COPY src ./src

# Compile and package application
RUN ./mvnw clean package -DskipTests

# STAGE 2: Lightweight, Production-Ready Runtime Image
FROM eclipse-temurin:21-jre-alpine

LABEL maintainer="Gayatri Patil <23102A0016@college.edu>"
LABEL description="Vaccination Reminder Portal DevOps Production Container"
LABEL version="1.0.0"

WORKDIR /app

# Create a secure, non-privileged system user and group
RUN addgroup -S vaxgroup && adduser -S vaxuser -G vaxgroup

# Install curl and wget for health checks
RUN apk add --no-cache curl wget

# Copy executable jar from builder stage
COPY --from=builder /build/target/vaccination-reminder-portal-1.0.0.jar app.jar

# Set ownership to unprivileged user
RUN chown -R vaxuser:vaxgroup /app

# Switch to non-root user
USER vaxuser

# Expose default HTTP port
EXPOSE 8080

# Configure container healthcheck
HEALTHCHECK --interval=20s --timeout=5s --start-period=30s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/api/health || exit 1

# Environment settings
ENV SPRING_PROFILES_ACTIVE=prod \
    JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseG1GC"

# Launch application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

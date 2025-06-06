# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim as builder

# Set the working directory in the container
WORKDIR /app

# Add the JAR file
ADD app.jar app.jar

# Run the application with a non-root user for better security
FROM openjdk:17-jdk-slim
WORKDIR /app

# Install curl for health checks
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Create a non-root user to run the application
RUN addgroup --system --gid 1001 appgroup && \
    adduser --system --uid 1001 --gid 1001 appuser

# Copy the JAR from the builder stage
COPY --from=builder /app/app.jar /app/app.jar
RUN chown -R appuser:appgroup /app

# Set the user to run the application
USER appuser

# Expose the port the app runs on
EXPOSE 8080

# Define health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=30s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

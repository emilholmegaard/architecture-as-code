# Use OpenJDK 23 as base image
FROM eclipse-temurin:23-jdk-jammy as builder

# Set working directory
WORKDIR /app

# Copy the project files
COPY . .

# Build the application
RUN ./mvnw clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:23-jre-jammy

WORKDIR /app

# Copy the jar from builder stage
COPY --from=builder /app/target/architecture-as-code-1.0.0.jar app.jar

# Expose the port the app runs on
EXPOSE 8080

# Command to run the application
CMD ["java", "-jar", "app.jar"]
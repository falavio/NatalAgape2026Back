# Use the official OpenJDK image as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the build.gradle.kts and settings.gradle.kts files
COPY build.gradle.kts settings.gradle.kts /app/

# Copy the gradle wrapper files
COPY gradlew /app/
COPY gradle /app/gradle

# Copy the source code
COPY src /app/src

# Download the dependencies and build the application
RUN ./gradlew build -x test

# Expose the port the app runs on
EXPOSE 8080

# Set the entry point to run the application
ENTRYPOINT ["java", "-jar", "build/libs/natalagape-backend-0.0.1-SNAPSHOT.jar"]
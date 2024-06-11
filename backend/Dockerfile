# Use a Gradle image to build the project
FROM gradle:8.7.0-jdk17 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy Gradle wrapper and project files to the container
COPY gradle/ gradle/
COPY gradlew .
COPY build.gradle.kts .
COPY settings.gradle.kts .

RUN ./gradlew dependencies

# Copy source code
COPY src/main src/main

RUN ./gradlew build -x test


# Use a minimal Java runtime image for the final image
FROM eclipse-temurin:17-jdk-jammy

# Set the working directory inside the container
WORKDIR /app

# Copy the jar file from the build stage to the final image
COPY --from=build /app/build/libs/*.jar app.jar

# Set the entry point to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

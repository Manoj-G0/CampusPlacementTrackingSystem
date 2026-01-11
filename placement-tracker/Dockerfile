# -----------------------------
# Stage 1: Build the WAR using Maven + JDK
# -----------------------------
FROM maven:3.9.2-eclipse-temurin-17 AS build

WORKDIR /app

# Copy pom.xml and source code
COPY pom.xml .
COPY src ./src

# Build the WAR (skip tests)
RUN mvn clean package -DskipTests

# -----------------------------
# Stage 2: Tomcat runtime
# -----------------------------
FROM tomcat:10.1-jdk17

# Remove default apps
RUN rm -rf /usr/local/tomcat/webapps/*

# Create temp upload folder for multipart uploads
RUN mkdir -p /usr/local/tomcat/tmp/uploads && chmod 777 /usr/local/tomcat/tmp/uploads

# Set Tomcat's temp directory to include uploads
ENV CATALINA_TMPDIR=/usr/local/tomcat/tmp

# Copy the WAR from the build stage
COPY --from=build /app/target/placement-tracker-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/placement-tracker.war

# Expose HTTP port
EXPOSE 8080

# Run Tomcat
CMD ["catalina.sh", "run"]

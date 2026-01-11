# ---------- STAGE 1: Build WAR ----------
FROM maven:3.9.9-eclipse-temurin-17 AS build

WORKDIR /app

# Copy pom.xml first (for dependency caching)
COPY pom.xml .

RUN mvn dependency:go-offline

# Copy source code
COPY src ./src

# Build WAR
RUN mvn clean package -DskipTests

# ---------- STAGE 2: Run on Tomcat ----------
FROM tomcat:10.1-jdk17

# Remove default apps
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy WAR from build stage
COPY --from=build /app/target/placement-tracker-0.0.1-SNAPSHOT.war \
    /usr/local/tomcat/webapps/placement-tracker.war

EXPOSE 8080

CMD ["catalina.sh", "run"]

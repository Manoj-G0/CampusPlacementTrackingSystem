# Use Maven + JDK to build WAR
FROM maven:3.9.2-eclipse-temurin-17 AS build

WORKDIR /app
COPY pom.xml .
COPY src ./src

# Build the WAR
RUN mvn clean package -DskipTests

# Use Tomcat image for runtime
FROM tomcat:10.1-jdk17

# Remove default apps
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy WAR from build stage
COPY --from=build /app/target/placement-tracker-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/placement-tracker.war

EXPOSE 8080
CMD ["catalina.sh", "run"]

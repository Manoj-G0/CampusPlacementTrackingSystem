FROM tomcat:10.1-jdk17

# Remove default apps
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy WAR as ROOT
COPY target/placement-tracker-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/placement-tracker.war

EXPOSE 8080

CMD ["catalina.sh", "run"]

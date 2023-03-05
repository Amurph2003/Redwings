FROM maven:3.6.0-jdk-11-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

FROM openjdk:18
COPY target/estore-api-0.0.1-SNAPSHOT.jar app/app.jar
EXPOSE 8080
CMD ["java", "-jar", "app/app.jar"]
FROM maven:3.6.0-jdk-11-slim AS build
COPY . /app
RUN mvn -f /app/pom.xml clean package

FROM openjdk:18
WORKDIR /app
COPY /target/estore-api-0.0.1-SNAPSHOT.jar /app
EXPOSE 8080
CMD ["java", "-jar", "estore-api-0.0.1-SNAPSHOT.jar"]
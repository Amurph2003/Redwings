FROM maven:3.6.0-jdk-11-slim AS build
FROM openjdk:11-jre-slim
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn compile exec:java
EXPOSE 5600
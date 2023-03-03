FROM maven:3.8.7-openjdk-18 
# FROM openjdk:11-jre-slim
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn compile exec:java
EXPOSE 5600
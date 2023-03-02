FROM maven:3.6.0-jdk-11-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn compile exec:java

FROM openjdk:11-jre-slim
COPY --from=build /home/app/target/Falcon-0.0.1.jar /usr/local/lib/falcon.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/falcon.jar"]
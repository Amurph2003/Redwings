FROM maven:3.8.7-openjdk-18 AS build
# FROM openjdk:11-jre-slim
WORKDIR /home/app
COPY src /home/app/src
COPY data /home/app/data
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

COPY --from=build /home/app/target/estore-api-0.0.1-SNAPSHOT.jar /usr/local/lib/estore.jar
EXPOSE 5600
ENTRYPOINT ["java","-jar","/usr/local/lib/falcon.jar"]
FROM maven:3.8.7-openjdk-18 AS build
# FROM openjdk:11-jre-slim
WORKDIR /home/app
COPY . /home/app
RUN ls /home/app/data
RUN mvn -f /home/app/pom.xml clean package

FROM openjdk:18
COPY --from=build /home/app/target/estore-api-0.0.1-SNAPSHOT.jar /usr/local/lib/Redwings.jar
# COPY --from=build /home/app/data /usr/local/lib
EXPOSE 5600
ENTRYPOINT ["java","-jar","/usr/local/lib/Redwings.jar"]
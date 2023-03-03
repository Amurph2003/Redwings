FROM maven:3.8.7-openjdk-18 AS build
# FROM openjdk:11-jre-slim
WORKDIR /home/app
COPY src /home/app/src
COPY data /home/app/data
COPY pom.xml /home/app
COPY Redwings.jar /home/app
RUN mvn -f /home/app/pom.xml clean package

FROM openjdk:18
COPY --from=build /home/app/Redwings.jar /usr/local/lib/Redwings.jar
EXPOSE 5600
ENTRYPOINT ["java","-jar","/usr/local/lib/Redwings.jar"]
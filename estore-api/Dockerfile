FROM maven:3.8.7-openjdk-18 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml package

FROM openjdk:18
COPY --from=build /home/app/target/estore-api-0.0.1-SNAPSHOT.jar /usr/local/lib/app.jar
EXPOSE 8080
CMD ["java", "-jar", "/usr/local/lib/app.jar"]
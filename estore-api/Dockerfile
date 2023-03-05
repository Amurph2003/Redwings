FROM maven:3.8.7-openjdk-18
RUN mvn package
COPY target/estore-api-0.0.1-SNAPSHOT.jar app/app.jar
EXPOSE 8080
CMD ["java", "-jar", "app/app.jar"]
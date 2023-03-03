FROM maven:3.8.7-openjdk-18 AS build
# FROM openjdk:11-jre-slim
WORKDIR /home/app
COPY . /home/app
RUN mvn -f /home/app/pom.xml clean package

FROM openjdk:18
COPY --from=build /home/app/target/estore-api-0.0.1-SNAPSHOT.jar /usr/local/lib/Redwings.jar
COPY /home/app/data/inventory.json /usr/local/lib/data
COPY /home/app/data/shopping-carts.json /usr/local/lib/data
EXPOSE 5600
ENTRYPOINT ["java","-jar","/usr/local/lib/Redwings.jar"]
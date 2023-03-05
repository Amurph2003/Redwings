FROM maven:3.8.7-openjdk-18
WORKDIR /home/app
COPY . /home/app
COPY pom.xml /home/app
ENTRYPOINT ["mvn", "compile", "exec:java"]
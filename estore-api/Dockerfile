FROM maven:3.8.7-openjdk-18
COPY . /home
ENTRYPOINT ["mvn", "-X", "compile", "exec:java"]
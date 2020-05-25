#FROM openjdk:8-jdk-alpine
#FROM maven:alpine
#
#VOLUME /tmp
#EXPOSE 8082
#RUN mkdir -p /app/
#RUN mkdir -p /app/logs/
#WORKDIR /app
#ADD pom.xml /app
#COPY . /app
#RUN mvn -v
#RUN mvn clean install -DskipTests
#ADD target/docker-spring-boot.jar /app/app.jar
#ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Dspring.profiles.active=container", "-jar", "/app/app.jar"]

#Specifying the base image, I'm using Java 11
FROM openjdk:11-slim

# where Spring Boot will store temporary files
VOLUME /tmp

# Sets up ports the container listens on: 8080 (spring boots default) and 5005 (debugger port)
EXPOSE 8080 5005

RUN mkdir -p /app/

RUN mkdir -p /app/logs/

# Copies the compiled jar file to the root of the container -- DOUBLE CHECK THIS.
# ADD target/docker-spring-boot.jar /app/parking.jar
# The application's jar file
ARG JAR_FILE=target/docker-spring-boot.jar

# Add the application's jar to the container
ADD ${JAR_FILE} /app/parking.jar

# Specifies the command that will start up the application.
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Dspring.profiles.active=default","-Xms1g","-Xmx1g","-jar","/app/parking.jar"]

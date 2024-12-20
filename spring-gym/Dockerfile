FROM openjdk:21-jdk-slim
ARG JAR_FILE=target/spring-gym-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} spring-gym.jar
EXPOSE 3002
ENTRYPOINT ["java", "-jar", "spring-gym.jar"]
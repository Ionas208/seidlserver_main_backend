FROM arm64v8/openjdk:17
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} backend.jar
EXPOSE 80
ENTRYPOINT ["java", "-jar", "backend.jar"]
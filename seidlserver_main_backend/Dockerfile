FROM hypriot/rpi-java
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} backend.jar
EXPOSE 80
ENTRYPOINT ["java", "-jar", "backend.jar"]
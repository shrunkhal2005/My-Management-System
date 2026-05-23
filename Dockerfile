FROM eclipse-temurin:21-jre

ARG JAR_FILE=target/jfx-app-1.0.jar
COPY ${JAR_FILE} /app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app.jar"]

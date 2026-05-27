FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /workspace
COPY pom.xml ./pom.xml
COPY src ./src
RUN mvn -B -DskipTests package

FROM eclipse-temurin:21-jre

WORKDIR /app
COPY --from=builder /workspace/target/*.jar /app/app.jar
ENTRYPOINT ["java","-jar","/app/app.jar"]

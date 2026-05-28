FROM mysql:8.0

# Copy initialization SQL and start script from mysql/ directory
COPY mysql/init.sql /docker-entrypoint-initdb.d/
COPY mysql/start.sh /start.sh

RUN chmod +x /start.sh

# Install Python for the lightweight health server
RUN apt-get update && apt-get install -y python3 \
    && rm -rf /var/lib/apt/lists/*

EXPOSE 8080

CMD ["/start.sh"]
FROM maven:3.9.9-eclipse-temurin-21 AS builder

ARG SERVICE_DIR=auth-service

WORKDIR /workspace
COPY ${SERVICE_DIR}/pom.xml ./pom.xml
COPY ${SERVICE_DIR}/src ./src
RUN mvn -B -DskipTests package

FROM eclipse-temurin:21-jre

WORKDIR /app
RUN apt-get update \
	&& apt-get install -y --no-install-recommends curl \
	&& rm -rf /var/lib/apt/lists/*
COPY --from=builder /workspace/target/*.jar /app/app.jar

ENTRYPOINT ["java","-jar","/app/app.jar"]

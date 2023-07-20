FROM maven:3.8.6-eclipse-temurin-17-focal AS final

LABEL authors="erikeduardojimenez"

WORKDIR /app
COPY . .

RUN mvn clean package -DskipTests

EXPOSE 8080

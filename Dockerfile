# Pasul 1: Compilare (Build)
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Pasul 2: Rulare (Runtime) - Folosim o imagine stabilă
FROM amazoncorretto:17-alpine
WORKDIR /app
# Copiem fișierul generat anterior
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
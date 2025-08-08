# Etap 1: budowanie aplikacji
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Kopiujemy pliki i budujemy JAR
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Etap 2: obraz produkcyjny z Javą
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Kopiujemy wygenerowany plik JAR z poprzedniego etapu
COPY --from=build /app/target/*.jar app.jar

# Port (Render i tak nadpisze PORT zmienną środowiskową)
EXPOSE 8080

# Uruchamianie aplikacji
ENTRYPOINT ["java", "-jar", "app.jar"]
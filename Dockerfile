# Etapa de construcción
FROM maven:3.9.9-amazoncorretto-17-alpine AS builder

WORKDIR /app

# Copiar archivos POM y descargar dependencias sin compilar
COPY pom.xml ./
COPY common/pom.xml common/
COPY domain/pom.xml domain/
COPY application/pom.xml application/
COPY maria-output-adapter/pom.xml maria-output-adapter/
COPY mongo-output-adapter/pom.xml mongo-output-adapter/
COPY rest-input-adapter/pom.xml rest-input-adapter/
COPY cli-input-adapter/pom.xml cli-input-adapter/

RUN mvn dependency:go-offline -B

# Copiar código fuente y construir el proyecto
COPY common/src common/src
COPY domain/src domain/src
COPY application/src application/src
COPY maria-output-adapter/src maria-output-adapter/src
COPY mongo-output-adapter/src mongo-output-adapter/src
COPY rest-input-adapter/src rest-input-adapter/src
COPY cli-input-adapter/src cli-input-adapter/src

# Construir el JAR
RUN mvn clean package -DskipTests

# Etapa de ejecución
FROM amazoncorretto:17.0.13-alpine

WORKDIR /PERSONAPP-HEXA-SPRING-BOOT

# Copiar el JAR desde la etapa de construcción
COPY --from=builder /app/rest-input-adapter/target/rest-input-adapter-0.0.1-SNAPSHOT.jar app.jar


EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=live"]
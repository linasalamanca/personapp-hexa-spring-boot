# Usamos una imagen base de Maven con JDK 11 de Amazon Corretto para compilar la aplicación
FROM maven:3.9.9-amazoncorretto-17-alpine AS builder

# Establecemos el directorio de trabajo
WORKDIR /app

# Copiamos el archivo pom.xml y descargamos las dependencias en modo offline
COPY pom.xml .
COPY common/pom.xml common/
COPY domain/pom.xml domain/
COPY application/pom.xml application/
COPY maria-output-adapter/pom.xml maria-output-adapter/
COPY mongo-output-adapter/pom.xml mongo-output-adapter/
COPY rest-input-adapter/pom.xml rest-input-adapter/
COPY cli-input-adapter/pom.xml cli-input-adapter/

RUN mvn dependency:go-offline -B

# Copia los archivos fuente de cada módulo
COPY common/src common/src
COPY domain/src domain/src
COPY application/src application/src
COPY maria-output-adapter/src maria-output-adapter/src
COPY mongo-output-adapter/src mongo-output-adapter/src
COPY rest-input-adapter/src rest-input-adapter/src
COPY cli-input-adapter/src cli-input-adapter/src
# Compilamos el proyecto completo
RUN mvn clean
RUN mvn install -DskipTests



# Dockerfile-rest
FROM amazoncorretto:17.0.13-alpine


# Directorio de trabajo dentro del contenedor
WORKDIR /PERSONAPP-HEXA-SPRING-BOOT

# Copia el archivo .jar específico del REST desde la carpeta local
COPY --from=builder /app/rest-input-adapter/target/*.jar app.jar

# Exponer el =uerto 8080 para el servicio REST
EXPOSE 8080

# Comando para ejecutar la aplicación REST
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=live"]

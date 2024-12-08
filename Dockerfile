# Imagen
FROM openjdk:23-jdk

# Definimos el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiamos el archivo pom.xml y otros archivos necesarios para la construcción
COPY ./pom.xml /app
COPY ./.mvn /app/.mvn
COPY ./mvnw /app

# Descargar las dependencias sin ejecutar pruebas
RUN ./mvnw dependency:go-offline

# Copiar el código fuente dentro del contenedor
COPY ./src /app/src

# Construir la aplicación
RUN ./mvnw clean install -DskipTests

# Copiar el archivo JAR generado desde la carpeta target a la carpeta de trabajo
COPY ./target/Rastreo-0.0.1-SNAPSHOT.jar /app/Rastreo-0.0.1-SNAPSHOT.jar

# Establecer el comando de inicio para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "/app/Rastreo-0.0.1-SNAPSHOT.jar"]


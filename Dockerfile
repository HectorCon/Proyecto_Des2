# Usa una imagen base de Java 17
FROM eclipse-temurin:17-jdk-alpine

# Establece el directorio de trabajo
WORKDIR /app

# Copia el archivo pom.xml y el wrapper de Maven
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Da permisos de ejecución al wrapper de Maven (para sistemas Unix)
RUN chmod +x ./mvnw

# Copia el código fuente
COPY src ./src

# Construye la aplicación
RUN ./mvnw clean package -DskipTests

# Expone el puerto 8080
EXPOSE 8080

# Comando para ejecutar la aplicación
CMD ["java", "-jar", "target/proyecto_des_web-0.0.1-SNAPSHOT.jar"]
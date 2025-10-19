# Usa la imagen base de Amazon Corretto 21
FROM amazoncorretto:21

# Establece el directorio de trabajo
WORKDIR /app

# Copia el archivo JAR desde la raíz del proyecto
COPY backend-0.0.1-SNAPSHOT.jar app.jar

# Expone el puerto que usa tu aplicación
EXPOSE 5000

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
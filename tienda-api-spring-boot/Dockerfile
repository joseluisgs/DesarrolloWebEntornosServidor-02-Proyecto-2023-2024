# Etapa de compilación, un docker especifico, que se etiqueta como build
FROM gradle:jdk17 AS build

# Directorio de trabajo
WORKDIR /app

# Define las variables de entorno para la etapa de compilación
# Se las pasamos en el docker-compose.yml con la instrucción environment
# ENV  API_PORT={$API_PORT:3000}
# ENV  DATABASE_USER={$DATABASE_USER:admin}
# ENV  DATABASE_PASSWORD={$DATABASE_PASSWORD:adminPassword123}
# ENV  POSTGRES_HOST={$POSTGRES_HOST:postgres-db}
# ENV  POSTGRES_PORT={$POSTGRES_PORT:5432}
# ENV  POSTGRES_DATABASE={$POSTGRES_DATABASE:tienda}
# ENV  MONGO_HOST={$MONGO_HOST:mongo-db}
# ENV  MONGO_PORT={$MONGO_PORT:27017}
# ENV  MONGO_DATABASE={$MONGO_DATABASE:tienda}
# ENV  PERFIL={$PERFIL:prod}

# Copia los archivos build.gradle y src de nuestro proyecto
COPY build.gradle.kts .
COPY gradlew .
COPY gradle gradle
COPY src src

# Podemos decirle que saque la documentación de los test (coverage)
# RUN ./gradlew test jacocoTestReport
# Podemos decirle que saque el javaDoc
# RUN ./gradlew javadoc

# Compila y construye el proyecto, podemos evitar los test evitando con -x test
# Cuidado con los test y el orden de ejecución con los contenedores
# Como primero ejecutamos la imagen antes que los contenedores de postgres y mongo
# No podemos ejecutar los test, ya que no existen las bases de datos
# Para eso deberíamos crear otro docker-compose.yml con los contenedores de postgres y mongo

RUN ./gradlew build -x test

# Etapa de ejecución, un docker especifico, que se etiqueta como run
# Con una imagen de java
FROM openjdk:17-jdk AS run

# Directorio de trabajo
WORKDIR /app

# Copia el jar de la aplicación, ojo que esta en la etapa de compilación, etiquetado como build
# Cuidado con la ruta definida cuando has copiado las cosas en la etapa de compilación
# Para copiar un archivo de una etapa a otra, se usa la instrucción COPY --from=etapaOrigen
COPY --from=build /app/build/libs/*SNAPSHOT.jar /app/my-app.jar

# Podemos copiar la documentación de los test (coverage) o el javaDoc
# COPY --from=build /app/build/reports/jacoco/test/html/ coverage
# COPY --from=build /app/build/docs/javadoc/ javadoc

# Define las variables de entorno para la etapa de ejecución
# Se las pasamos en el docker-compose.yml con la instrucción environment
# ENV  API_PORT={$API_PORT:3000}
# ENV  DATABASE_USER={$DATABASE_USER:admin}
# ENV  DATABASE_PASSWORD={$DATABASE_PASSWORD:adminPassword123}
# ENV  POSTGRES_HOST={$POSTGRES_HOST:postgres-db}
# ENV  POSTGRES_PORT={$POSTGRES_PORT:5432}
# ENV  POSTGRES_DATABASE={$POSTGRES_DATABASE:tienda}
# ENV  MONGO_HOST={$MONGO_HOST:mongo-db}
# ENV  MONGO_PORT={$MONGO_PORT:27017}
# ENV  MONGO_DATABASE={$MONGO_DATABASE:tienda}
# ENV  PERFIL={$PERFIL:prod}

# Expone el puerto 3000
EXPOSE 3000
# Ejecuta el jar
# Para lanzarlo con un perfil distinto al fijado por defecto, se usa la instrucción
# ENTRYPOINT ["java","-jar","-Dspring.profiles.active=dev","/app/my-app.jar"]
ENTRYPOINT ["java","-jar","/app/my-app.jar"]
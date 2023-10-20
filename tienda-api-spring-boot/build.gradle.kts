plugins {
    java // Plugin de Java
    id("org.springframework.boot") version "3.1.4" // Versión de Spring Boot
    id("io.spring.dependency-management") version "1.1.3" // Gestión de dependencias
}

group = "dev.joseluisgs"
version = "0.0.1-SNAPSHOT"

java {
    // versión de Java
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

dependencies {
    // Dependencias de Spring Web for HTML Apps y Rest
    implementation("org.springframework.boot:spring-boot-starter-web")
    // Spring Data JPA
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    // Cache
    implementation("org.springframework.boot:spring-boot-starter-cache")
    // Validación
    implementation("org.springframework.boot:spring-boot-starter-validation")
    // Websocket
    implementation("org.springframework.boot:spring-boot-starter-websocket")

    // Lombok
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // H2 Database
    runtimeOnly("com.h2database:h2")

    // Para usar con jackson el controlador las fechas: LocalDate, LocalDateTime, etc
    // Lo podemos usar en el test o en el controlador, si hiciese falta, por eso está aquí
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")

    // Para pasar a XML los responses, negocacion de contenido
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml")


    // Dependencias para Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")

}

tasks.withType<Test> {
    useJUnitPlatform() // Usamos JUnit 5
}

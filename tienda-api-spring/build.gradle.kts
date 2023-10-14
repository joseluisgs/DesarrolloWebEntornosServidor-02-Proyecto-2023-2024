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

dependencies {
    // Dependencias de Spring Web for HTML Apps y Rest
    implementation("org.springframework.boot:spring-boot-starter-web")

    // Dependencias para Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
    useJUnitPlatform() // Usamos JUnit 5
}

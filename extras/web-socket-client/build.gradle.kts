plugins {
    id("java")
}

group = "dev.joseluisgs"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("javax.websocket:javax.websocket-api:1.1")
    implementation("org.glassfish.tyrus:tyrus-container-jdk-client:1.14")
    implementation("org.glassfish.tyrus:tyrus-core:1.14")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}


tasks.test {
    useJUnitPlatform()
}
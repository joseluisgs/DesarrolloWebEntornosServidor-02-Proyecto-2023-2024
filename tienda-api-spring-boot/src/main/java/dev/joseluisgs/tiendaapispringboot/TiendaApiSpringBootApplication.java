package dev.joseluisgs.tiendaapispringboot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching // Habilitamos el caché a nivel de aplicación
@Slf4j
public class TiendaApiSpringBootApplication implements CommandLineRunner {

    public static void main(String[] args) {
        // Iniciamos la aplicación de Spring Boot
        SpringApplication.run(TiendaApiSpringBootApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // Aquí podemos ejecutar código al arrancar la aplicación
        // Este mensaje simplemente es para que lo veas en la consola,
        // no es necesario hacer este método si no lo vas a usar
        System.out.println("🟢 Servidor arrancado 🚀");
    }
}

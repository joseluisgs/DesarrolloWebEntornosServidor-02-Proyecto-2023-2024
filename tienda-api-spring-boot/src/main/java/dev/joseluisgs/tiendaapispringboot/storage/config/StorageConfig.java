package dev.joseluisgs.tiendaapispringboot.storage.config;

import dev.joseluisgs.tiendaapispringboot.storage.StorageService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de almacenamiento
 */
@Configuration
@Slf4j
public class StorageConfig {
    @Autowired
    private StorageService storageService;

<<<<<<< HEAD
    @Bean
    public CommandLineRunner init(StorageService storageService, @Value("${upload.delete}") String deleteAll) {
        return args -> {
            // Inicializamos el servicio de ficheros
            // Leemos de application.properties si necesitamos borrar todo o no
            if (deleteAll.equals("true")) {
                log.info("Borrando ficheros de almacenamiento...");
                storageService.deleteAll();
            }
            storageService.init(); // inicializamos
        };
=======
    @Value("${upload.delete}")
    private String deleteAll;

    @PostConstruct
    public void init() {
        if (deleteAll.equals("true")) {
            log.info("Borrando ficheros de almacenamiento...");
            storageService.deleteAll();
        }

        storageService.init(); // inicializamos
>>>>>>> ficheros
    }
}